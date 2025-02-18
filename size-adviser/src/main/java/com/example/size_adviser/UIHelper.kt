package com.example.size_adviser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

//That's a workaround and we definitely can do it better. I don't like the idea of DataBridge
// shared instance and how we display the compose content, but let it be as is for now
internal class UIHelper {

    enum class ComposeType() {
        INPUT, RESULT
    }

    companion object {
        internal const val KEY_TYPE = "content_type"
        internal const val KEY_SIZE = "size_value"


        suspend fun displayInputDialog(context: Context): InputData? {
            context.startActivity(Intent(context, WrapperActivity::class.java).apply {
                putExtra(KEY_TYPE, ComposeType.INPUT.name)
            })

            return DataBridge.shared.updateFlow.first()
        }

        fun displayResultDialog(context: Context, size: SizeAdviser.ProposedSize) {
            context.startActivity(Intent(context, WrapperActivity::class.java).apply {
                putExtra(KEY_TYPE, ComposeType.RESULT.name)
                putExtra(KEY_SIZE, size.name)
            })
        }
    }
}

private class DataBridge {

    val updateFlow = MutableSharedFlow<InputData?>(extraBufferCapacity = 1)

    companion object {
        val shared = DataBridge()
    }
}

internal class WrapperActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type: UIHelper.ComposeType? = intent
            .getStringExtra(UIHelper.KEY_TYPE)
            ?.let { UIHelper.ComposeType.valueOf(it) }

        if (type == null) {
            finish()
            return
        }

        setContent {
            when(type) {
                UIHelper.ComposeType.INPUT -> InputDialog {
                    DataBridge.shared.updateFlow.tryEmit(it)
                    finish()
                }
                UIHelper.ComposeType.RESULT -> {
                    val size = intent
                        .getStringExtra(UIHelper.KEY_SIZE)
                        ?.let { SizeAdviser.ProposedSize.valueOf(it) }

                    if (size == null) {
                        finish()
                        return@setContent
                    }

                    ResultDialog(size, ::finish)
                }
            }
        }
    }
}