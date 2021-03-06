/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui.fakeandroidview

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.LayoutRes
import androidx.compose.Composable

/**
 * Composes an Android [View] given a layout resource [resId]. The method handles the inflation
 * of the [View] and will call the [postInflationCallback] after this happens. Note that the
 * callback will always be invoked on the main thread.
 *
 * @param resId The id of the layout resource to be inflated.
 * @param postInflationCallback The callback to be invoked after the layout is inflated.
 */
@Composable
// TODO(popam): support modifiers here
fun AndroidView(@LayoutRes resId: Int, postInflationCallback: (View) -> Unit = { _ -> }) {
    AndroidViewHolder(postInflationCallback = postInflationCallback, resId = resId)
}


private class AndroidViewHolder(context: Context) : ViewGroup(context) {
    var view: View? = null
        set(value) {
            if (value != field) {
                field = value
                removeAllViews()
                addView(view)
            }
        }

    var postInflationCallback: (View) -> Unit = {}

    var resId: Int? = null
        set(value) {
            if (value != field) {
                field = value
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(resId!!, this, false)
                this.view = view
                postInflationCallback(view)
            }
        }

    init {
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        view?.measure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(view?.measuredWidth ?: 0, view?.measuredHeight ?: 0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        view?.layout(l, t, r, b)
    }

    override fun getLayoutParams(): LayoutParams? {
        return view?.layoutParams ?: LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     *
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * [.performClick]. This will ensure consistent system behavior,
     * including:
     *
     *  * obeying click sound preferences
     *  * dispatching OnClickListener calls
     *  * handling [ACTION_CLICK][AccessibilityNodeInfo.ACTION_CLICK] when
     * accessibility features are enabled
     *
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}
