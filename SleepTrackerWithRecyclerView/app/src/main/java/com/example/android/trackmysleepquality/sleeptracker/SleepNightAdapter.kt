/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(private val clickListener: SleepNightListener) :
        ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    /**
     * Submit list of data items, containing a Header and map input SleepNight
     * to SleepNightItems.
     */
    fun addHeaderAndSubmitList(list: List<SleepNight>) {
        adapterScope.launch {
            val items = listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    /**
     * Differentiate item view types so recycled ViewHolders are only bound to
     * compatible items
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    /**
     * Bind data to a ViewHolder at position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.SleepNightItem
                holder.bind(item.sleepNight, clickListener)
            }
        }
    }

    /**
     * Create appropriate ViewHolder for an item, given
     * the items viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    /**
     * ViewHolder that holds a single [ListItemSleepNightBinding], for displaying SleepNight.
     *
     * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     */
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleepNight = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    /**
     * ViewHolder that holds a single [TextView], for displaying header.
     */
    class TextItemViewHolder private constructor(textView: TextView) : RecyclerView.ViewHolder(textView) {
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.header, parent, false) as TextView
                return TextItemViewHolder(view)
            }
        }
    }
}

/**
 * Provide methods for calculating differences in data
 */
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    /**
     * Items may be the same, but contents may need to be updated
     */
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return if (oldItem is DataItem.SleepNightItem &&
                newItem is DataItem.SleepNightItem) {
            oldItem.sleepNight == newItem.sleepNight
        } else {
            oldItem.id == newItem.id
        }
    }

}

class SleepNightListener(val clickListener: (nightId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

/**
 * Sealed classes provide restricted set of types, similar to enums,
 * as subclasses must be defined in same file, but allows
 * freedom of representation of abstract classes.
 */
sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id: Long
            get() = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    /**
     * DataItems require an id, used by DiffUtil
     */
    abstract val id: Long
}