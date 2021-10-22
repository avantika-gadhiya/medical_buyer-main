/*
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file kt in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bluesharklabs.com.medical.utils

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.AbsListView
import android.widget.HeaderViewListAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SectionIndexer

import bluesharklabs.com.medical.BuildConfig


/**
 * ListView, which is capable to pin section views at its top while the rest is still scrolled.
 */
class PinnedSectionListView : ListView {

    //-- class fields

    // fields used for handling touch events
    private val mTouchRect = Rect()
    private val mTouchPoint = PointF()
    private var mTouchSlop: Int = 0
    private var mTouchTarget: View? = null
    private var mDownEvent: MotionEvent? = null

    // fields used for drawing shadow under a pinned section
    private var mShadowDrawable: GradientDrawable? = null
    private var mSectionsDistanceY: Int = 0
    private var mShadowHeight: Int = 0

    /** Delegating listener, can be null.  */
    internal var mDelegateOnScrollListener: AbsListView.OnScrollListener? = null

    /** Shadow for being recycled, can be null.  */
    internal var mRecycleSection: PinnedSection? = null

    /** shadow instance with a pinned view, can be null.  */
    internal var mPinnedSection: PinnedSection? = null

    /** Pinned view Y-translation. We use it to stick pinned view to the next section.  */
    internal var mTranslateY: Int = 0

    /** Scroll listener which does the magic  */
    private val mOnScrollListener = object : AbsListView.OnScrollListener {

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener!!.onScrollStateChanged(view, scrollState)
            }
        }

        override fun onScroll(
            view: AbsListView,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {

            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener!!.onScroll(
                    view,
                    firstVisibleItem,
                    visibleItemCount,
                    totalItemCount
                )
            }

            // get expected adapter or fail fast
            val adapter = adapter
            if (adapter == null || visibleItemCount == 0) return  // nothing to do

            val isFirstVisibleItemSection =
                isItemViewTypePinned(adapter, adapter.getItemViewType(firstVisibleItem))

            if (isFirstVisibleItemSection) {
                val sectionView = getChildAt(0)
                if (sectionView.top == paddingTop) { // view sticks to the top, no need for pinned shadow
                    destroyPinnedShadow()
                } else { // section doesn't stick to the top, make sure we have a pinned shadow
                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount)
                }

            } else { // section is not at the first visible position
                val sectionPosition = findCurrentSectionPosition(firstVisibleItem)
                if (sectionPosition > -1) { // we have section position
                    ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount)
                } else { // there is no section for the first visible item, destroy shadow
                    destroyPinnedShadow()
                }
            }
        }

    }

    /** Default change observer.  */
    private val mDataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            recreatePinnedShadow()
        }

        override fun onInvalidated() {
            recreatePinnedShadow()
        }
    }

    //-- inner classes

    /** List adapter to be implemented for being used with PinnedSectionListView adapter.  */
    interface PinnedSectionListAdapter : ListAdapter {
        /** This method shall return 'true' if views of given type has to be pinned.  */
        fun isItemViewTypePinned(viewType: Int): Boolean
    }

    /** Wrapper class for pinned section view and its position in the list.  */
    internal class PinnedSection {
        var view: View? = null
        var position: Int = 0
        var id: Long = 0
    }

    //-- constructors

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    private fun initView() {
        setOnScrollListener(mOnScrollListener)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        setShadowVisible(true)
    }

    //-- public API methods

    fun setShadowVisible(visible: Boolean) {
        initShadow(visible)
        if (mPinnedSection != null) {
            val v = mPinnedSection!!.view
            invalidate(v!!.left, v.top, v.right, v.bottom + mShadowHeight)
        }
    }

    //-- pinned section drawing methods

    fun initShadow(visible: Boolean) {
        if (visible) {
            if (mShadowDrawable == null) {
                mShadowDrawable = GradientDrawable(
                    Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.parseColor("#ffa0a0a0"),
                        Color.parseColor("#50a0a0a0"),
                        Color.parseColor("#00a0a0a0")
                    )
                )
                mShadowHeight = (0 * resources.displayMetrics.density).toInt()
            }
        } else {
            if (mShadowDrawable != null) {
                mShadowDrawable = null
                mShadowHeight = 0
            }
        }
    }

    /** Create shadow wrapper with a pinned view for a view at given position  */
    internal fun createPinnedShadow(position: Int) {

        // try to recycle shadow
        var pinnedShadow = mRecycleSection
        mRecycleSection = null

        // create new shadow, if needed
        if (pinnedShadow == null) pinnedShadow = PinnedSection()
        // request new view using recycled view, if such
        val pinnedView = adapter.getView(position, pinnedShadow.view, this@PinnedSectionListView)

        // read layout parameters
        var layoutParams: ViewGroup.LayoutParams? =
            pinnedView.layoutParams
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams()
            pinnedView.layoutParams = layoutParams
        }

        var heightMode = View.MeasureSpec.getMode(layoutParams!!.height)
        var heightSize = View.MeasureSpec.getSize(layoutParams.height)

        if (heightMode == View.MeasureSpec.UNSPECIFIED) heightMode = View.MeasureSpec.EXACTLY

        val maxHeight = height - listPaddingTop - listPaddingBottom
        if (heightSize > maxHeight) heightSize = maxHeight

        // measure & layout
        val ws = View.MeasureSpec.makeMeasureSpec(
            width - listPaddingLeft - listPaddingRight,
            View.MeasureSpec.EXACTLY
        )
        val hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)

        // changed -> pinnedView.measure(ws, hs)
        pinnedView.measure(ws, 0)

        pinnedView.layout(0, 0, pinnedView.measuredWidth, pinnedView.measuredHeight)
        mTranslateY = 0

        // initialize pinned shadow
        pinnedShadow.view = pinnedView
        pinnedShadow.position = position
        pinnedShadow.id = adapter.getItemId(position)

        // store pinned shadow
        mPinnedSection = pinnedShadow
    }

    /** Destroy shadow wrapper for currently pinned view  */
    internal fun destroyPinnedShadow() {
        if (mPinnedSection != null) {
            // keep shadow for being recycled later
            mRecycleSection = mPinnedSection
            mPinnedSection = null
        }
    }

    /** Makes sure we have an actual pinned shadow for given position.  */
    internal fun ensureShadowForPosition(
        sectionPosition: Int,
        firstVisibleItem: Int,
        visibleItemCount: Int
    ) {
        if (visibleItemCount < 2) { // no need for creating shadow at all, we have a single visible item
            destroyPinnedShadow()
            return
        }

        if (mPinnedSection != null && mPinnedSection!!.position != sectionPosition) { // invalidate shadow, if required
            destroyPinnedShadow()
        }

        if (mPinnedSection == null) { // create shadow, if empty
            createPinnedShadow(sectionPosition)
        }

        // align shadow according to next section position, if needed
        val nextPosition = sectionPosition + 1
        if (nextPosition < count) {
            val nextSectionPosition = findFirstVisibleSectionPosition(
                nextPosition,
                visibleItemCount - (nextPosition - firstVisibleItem)
            )
            if (nextSectionPosition > -1) {
                val nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem)
                val bottom = mPinnedSection!!.view!!.bottom + paddingTop
                mSectionsDistanceY = nextSectionView.top - bottom
                if (mSectionsDistanceY < 0) {
                    // next section overlaps pinned shadow, move it up
                    mTranslateY = mSectionsDistanceY
                } else {
                    // next section does not overlap with pinned, stick to top
                    mTranslateY = 0
                }
            } else {
                // no other sections are visible, stick to top
                mTranslateY = 0
                mSectionsDistanceY = Integer.MAX_VALUE
            }
        }

    }

    internal fun findFirstVisibleSectionPosition(
        firstVisibleItem: Int,
        visibleItemCount: Int
    ): Int {
        var visibleItemCount = visibleItemCount
        val adapter = adapter

        val adapterDataCount = adapter.count
        if (lastVisiblePosition >= adapterDataCount) return -1 // dataset has changed, no candidate

        if (firstVisibleItem + visibleItemCount >= adapterDataCount) {//added to prevent index Outofbound (in case)
            visibleItemCount = adapterDataCount - firstVisibleItem
        }

        for (childIndex in 0 until visibleItemCount) {
            val position = firstVisibleItem + childIndex
            val viewType = adapter.getItemViewType(position)
            if (isItemViewTypePinned(adapter, viewType)) return position
        }
        return -1
    }

    internal fun findCurrentSectionPosition(fromPosition: Int): Int {
        val adapter = adapter

        if (fromPosition >= adapter.count) return -1 // dataset has changed, no candidate

        if (adapter is SectionIndexer) {
            // try fast way by asking section indexer
            val indexer = adapter as SectionIndexer
            val sectionPosition = indexer.getSectionForPosition(fromPosition)
            val itemPosition = indexer.getPositionForSection(sectionPosition)
            val typeView = adapter.getItemViewType(itemPosition)
            if (isItemViewTypePinned(adapter, typeView)) {
                return itemPosition
            } // else, no luck
        }

        // try slow way by looking through to the next section item above
        for (position in fromPosition downTo 0) {
            val viewType = adapter.getItemViewType(position)
            if (isItemViewTypePinned(adapter, viewType)) return position
        }
        return -1 // no candidate found
    }

    internal fun recreatePinnedShadow() {
        destroyPinnedShadow()
        val adapter = adapter
        if (adapter != null && adapter.count > 0) {
            val firstVisiblePosition = firstVisiblePosition
            val sectionPosition = findCurrentSectionPosition(firstVisiblePosition)
            if (sectionPosition == -1) return  // no views to pin, exit
            ensureShadowForPosition(
                sectionPosition,
                firstVisiblePosition, lastVisiblePosition - firstVisiblePosition
            )
        }
    }

    override fun setOnScrollListener(listener: AbsListView.OnScrollListener) {
        if (listener === mOnScrollListener) {
            super.setOnScrollListener(listener)
        } else {
            mDelegateOnScrollListener = listener
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        post {
            // restore pinned view after configuration change
            recreatePinnedShadow()
        }
    }

    override fun setAdapter(adapter: ListAdapter?) {

        // assert adapter in debug mode
        if (BuildConfig.DEBUG && adapter != null) {
            if (adapter !is PinnedSectionListAdapter)
                throw IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?")
            if (adapter.viewTypeCount < 2)
                throw IllegalArgumentException("Does your adapter handle at least two types" + " of views in getViewTypeCount() method: items and sections?")
        }

        // unregister observer at old adapter and register on new one
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterDataSetObserver(mDataSetObserver)
        adapter?.registerDataSetObserver(mDataSetObserver)

        // destroy pinned shadow, if new adapter is not same as old one
        if (oldAdapter !== adapter) destroyPinnedShadow()

        super.setAdapter(adapter)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mPinnedSection != null) {
            val parentWidth = r - l - paddingLeft - paddingRight
            val shadowWidth = mPinnedSection!!.view!!.width
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (mPinnedSection != null) {

            // prepare variables
            val pLeft = listPaddingLeft
            val pTop = listPaddingTop
            val view = mPinnedSection!!.view

            // draw child
            canvas.save()

            val clipHeight = view!!.height + if (mShadowDrawable == null) 0 else Math.min(
                mShadowHeight,
                mSectionsDistanceY
            )
            canvas.clipRect(pLeft, pTop, pLeft + view.width, pTop + clipHeight)

            canvas.translate(pLeft.toFloat(), (pTop + mTranslateY).toFloat())
            drawChild(canvas, mPinnedSection!!.view, drawingTime)

            if (mShadowDrawable != null && mSectionsDistanceY > 0) {
                mShadowDrawable!!.setBounds(
                    mPinnedSection!!.view!!.left,
                    mPinnedSection!!.view!!.bottom,
                    mPinnedSection!!.view!!.right,
                    mPinnedSection!!.view!!.bottom + mShadowHeight
                )
                mShadowDrawable!!.draw(canvas)
            }

            canvas.restore()
        }
    }

    //-- touch handling methods

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        val x = ev.x
        val y = ev.y
        val action = ev.action

        if (action == MotionEvent.ACTION_DOWN
            && mTouchTarget == null
            && mPinnedSection != null
            && isPinnedViewTouched(mPinnedSection!!.view!!, x, y)
        ) { // create touch target

            // user touched pinned view
            mTouchTarget = mPinnedSection!!.view
            mTouchPoint.x = x
            mTouchPoint.y = y

            // copy down event for eventually be used later
            mDownEvent = MotionEvent.obtain(ev)
        }

        if (mTouchTarget != null) {
            if (isPinnedViewTouched(mTouchTarget!!, x, y)) { // forward event to pinned view
                mTouchTarget!!.dispatchTouchEvent(ev)
            }

            if (action == MotionEvent.ACTION_UP) { // perform onClick on pinned view
                super.dispatchTouchEvent(ev)
                performPinnedItemClick()
                clearTouchTarget()

            } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
                clearTouchTarget()

            } else if (action == MotionEvent.ACTION_MOVE) {
                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

                    // cancel sequence on touch target
                    val event = MotionEvent.obtain(ev)
                    event.action = MotionEvent.ACTION_CANCEL
                    mTouchTarget!!.dispatchTouchEvent(event)
                    event.recycle()

                    // provide correct sequence to super class for further handling
                    super.dispatchTouchEvent(mDownEvent)
                    super.dispatchTouchEvent(ev)
                    clearTouchTarget()

                }
            }

            return true
        }

        // call super if this was not our pinned view
        return super.dispatchTouchEvent(ev)
    }

    private fun isPinnedViewTouched(view: View, x: Float, y: Float): Boolean {
        view.getHitRect(mTouchRect)

        // by taping top or bottom padding, the list performs on click on a border item.
        // we don't add top padding here to keep behavior consistent.
        mTouchRect.top += mTranslateY

        mTouchRect.bottom += mTranslateY + paddingTop
        mTouchRect.left += paddingLeft
        mTouchRect.right -= paddingRight
        return mTouchRect.contains(x.toInt(), y.toInt())
    }

    private fun clearTouchTarget() {
        mTouchTarget = null
        if (mDownEvent != null) {
            mDownEvent!!.recycle()
            mDownEvent = null
        }
    }

    private fun performPinnedItemClick(): Boolean {
        if (mPinnedSection == null) return false

        val listener = onItemClickListener
        if (listener != null && adapter.isEnabled(mPinnedSection!!.position)) {
            val view = mPinnedSection!!.view
            playSoundEffect(SoundEffectConstants.CLICK)
            view?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED)
            listener.onItemClick(this, view, mPinnedSection!!.position, mPinnedSection!!.id)
            return true
        }
        return false
    }

    companion object {

        fun isItemViewTypePinned(adapter: ListAdapter?, viewType: Int): Boolean {
            var adapter = adapter
            if (adapter is HeaderViewListAdapter) {
                adapter = adapter.wrappedAdapter
            }
            return (adapter as PinnedSectionListAdapter).isItemViewTypePinned(viewType)
        }
    }

}
