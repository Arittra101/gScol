package org.getscol.gscol.core.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.getscol.gscol.auth.data.GoogleAuthProvider
import org.getscol.gscol.auth.data.GoogleAuthToken
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

//class AndroidGoogleAuthProvider(private val context: Context) : GoogleAuthProvider {
//    private val credentialManager = CredentialManager.create(context)
//
//    override suspend fun getGoogleAuthToken(): Result<GoogleAuthToken, DataError.Remote> {
//        return try {
//
//            val googleIdOption = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(false)
//                .setAutoSelectEnabled(false)
//                .setServerClientId("713761866304-emsf2tj31u424fc48f64s9tafmnb9d78.apps.googleusercontent.com") // Fixed!
//                .build()
//
//            println("googleIdOption ${googleIdOption}")
//
//            val request = GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build()
//
//            println("request ${request}")
//
//
//            val result = credentialManager.getCredential(
//                request = request,
//                context = context
//            )
//
//            println("result== ${result}")
//
//
//            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
//            val idToken = credential.idToken
//
//            println("idToken ${idToken}")
//
//            return Result.Success(GoogleAuthToken(googleToken = idToken))
//        } catch (e: Exception) {
//            println("error ${e}")
//            Result.Error(DataError.Remote.GOOGLE_PLAY_SERVICE_UNAVAILABLE)
//        }
//    }
//}

class AndroidGoogleAuthProvider(private val context: Context) : GoogleAuthProvider {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun getGoogleAuthToken(): Result<GoogleAuthToken, DataError.Remote> {
        return try {
            println("Step 1: Creating GoogleIdOption")

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId("713761866304-emsf2tj31u424fc48f64s9tafmnb9d78.apps.googleusercontent.com")
                .build()

            println("Step 2: GoogleIdOption created = ${googleIdOption}")

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            println("Step 3: Request created = ${request}")
            println("Step 4: Calling getCredential...")

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            println("Step 5: Got result = ${result}")

            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = credential.idToken

            println("Step 6: Got idToken ${idToken}")

            Result.Success(GoogleAuthToken(googleToken = idToken))

        } catch (e: NoCredentialException) {
            println("ERROR: NoCredentialException - No credentials available")
            println("ERROR Details: ${e.message}")
            println("ERROR Type: ${e.type}")
            e.printStackTrace()
            Result.Error(DataError.Remote.GOOGLE_PLAY_SERVICE_UNAVAILABLE)

        } catch (e: GetCredentialCancellationException) {
            println("ERROR: User cancelled the sign-in")
            println("ERROR Details: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.GOOGLE_PLAY_SERVICE_UNAVAILABLE)

        } catch (e: GetCredentialException) {
            println("ERROR: GetCredentialException")
            println("ERROR Type: ${e.type}")
            println("ERROR Message: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.GOOGLE_PLAY_SERVICE_UNAVAILABLE)

        } catch (e: Exception) {
            println("ERROR: Unexpected exception - ${e.javaClass.simpleName}")
            println("ERROR Message: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.GOOGLE_PLAY_SERVICE_UNAVAILABLE)
        }
    }
}