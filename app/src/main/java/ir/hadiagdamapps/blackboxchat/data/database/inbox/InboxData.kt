package ir.hadiagdamapps.blackboxchat.data.database.inbox

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.Error
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EKeyGenerator.toText
import ir.hadiagdamapps.blackboxchat.data.database.generateWhereQuery
import ir.hadiagdamapps.blackboxchat.data.database.getBoolean
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PrivateKey
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns.*
import ir.hadiagdamapps.blackboxchat.data.database.put

class InboxData(context: Context) : DatabaseHelper(context, Table.INBOXES) {

    fun newInbox(pin: Pin): InboxModel? {
        var model: InboxModel?

        while (true) {
            try {

                val pair = E2EKeyGenerator.generateKeyPair()

                val salt = AesKeyGenerator.generateSalt()
                val aesKey = AesKeyGenerator.generateKey(pin.toString(), salt)

                val (privateKey, iv) = AesEncryptor.encryptMessage(
                    message = pair.private.toText(),
                    aesKey = aesKey
                )

                val publicKey = PublicKey.parse(pair.public)

                model = InboxModel(
                    inboxId = -1,
                    inboxPublicKey = publicKey,
                    inboxPrivateKey = PrivateKey.parse(privateKey)
                        ?: throw Error.PRIVATE_KEY_PARSE_ERROR,
                    label = Label.create(publicKey),
                    hasNewMessage = false,
                    iv = iv,
                    salt = salt
                )

                val db = writableDatabase

                return model.copy(inboxId = db.insert(table.tableName, null, ContentValues().apply {

                    put(INBOX_PUBLIC_KEY, model.inboxPublicKey.display())
                    put(INBOX_PRIVATE_KEY, model.inboxPrivateKey.toString())
                    put(LABEL, model.label.display())
                    put(SALT, model.salt)
                    put(IV, model.iv)

                }).apply {
                    db.close()
                })

            } catch (e: SQLiteConstraintException) {
                // reaches when PublicKey was not unique
            } catch (e: Exception) {
                return null
            }
        }
    }

    fun getInboxes(
        where: HashMap<InboxColumns, String>? = null
    ): List<InboxModel> {

        val c = readableDatabase.rawQuery(
            """
            SELECT
                $INBOX_ID,
                $INBOX_PUBLIC_KEY,
                $INBOX_PRIVATE_KEY,
                $LABEL,
                $HAS_NEW_MESSAGE,
                $IV,
                $SALT
            from ${table.tableName}
            ${generateWhereQuery(where)}
            
        """.trimIndent(), where?.values?.toTypedArray()
        )

        Log.e("cursor size", c.count.toString())
        return ArrayList<InboxModel>().apply {

            if (c.moveToFirst()) do
                add(
                    InboxModel(
                        inboxId = c.getLong(0),
                        inboxPublicKey = PublicKey.parse(c.getString(1)) ?: continue,
                        inboxPrivateKey = PrivateKey.parse(c.getString(2)) ?: continue,
                        label = Label.create(c.getString(3)) ?: continue,
                        hasNewMessage = c.getBoolean(4),
                        iv = c.getString(5),
                        salt = c.getString(6)
                    )
                )
            while (c.moveToNext())

            c.close()
        }
    }

    fun deleteInbox(inboxId: Long) {
        writableDatabase.delete(table.tableName, "$INBOX_ID = ?", arrayOf(inboxId.toString()))
    }

    fun updateLabel(label: Label, inboxId: Long) {
        writableDatabase.update(table.tableName, ContentValues().apply {
            put(LABEL, label.display())
        }, "$INBOX_ID = ?", arrayOf(inboxId.toString()))
    }
}
