package remainder.chronos.core.util

import android.content.Context
import android.widget.Toast

object UiMessage {

    fun showToast(context : Context , message : String  ){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}