package com.example.taskplanner.view.adapter

import android.text.Spanned
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.R
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.ItemTaskMedicationBinding
import com.example.taskplanner.databinding.ItemTaskNoteBinding
import com.example.taskplanner.databinding.ItemTaskProductsBinding
import com.example.taskplanner.databinding.ItemTaskReminderBinding

class AllNotesListAdapter(
    private val onDeleteNote: (TypeNotes) -> Unit,
    private val onClickChangeFinishedProduct: (TypeNotes, Int) -> Unit,
    private val onClickChangeFinishedNote: (TypeNotes) -> Unit,
    private val onChangeNote: (TypeNotes) -> Unit,
    private val day: Day
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_NOTE -> {
                NoteViewHolder(ItemTaskNoteBinding.inflate(LayoutInflater.from(parent.context)))
            }

            TYPE_PRODUCTS -> {
                ProductViewHolder(ItemTaskProductsBinding.inflate(LayoutInflater.from(parent.context)))
            }

            TYPE_REMINDER -> {
                ReminderViewHolder(ItemTaskReminderBinding.inflate(LayoutInflater.from(parent.context)))
            }

            TYPE_MEDICATION -> {
                MedicationViewHolder(ItemTaskMedicationBinding.inflate(LayoutInflater.from(parent.context)))
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return day.list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (day.list[position]) {
            is Medications -> TYPE_MEDICATION
            is Note -> TYPE_NOTE
            is Products -> TYPE_PRODUCTS
            is Reminder -> TYPE_REMINDER
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemNote = day.list[position]

        when (holder) {
            is NoteViewHolder -> {
                showNote(itemNote, holder)
            }

            is ProductViewHolder -> {
                showProduct(itemNote, holder)
            }

            is ReminderViewHolder -> {
                showReminder(itemNote, holder)
            }

            is MedicationViewHolder -> {
                showMedication(itemNote, holder)
            }
        }

    }

    private fun showNote(itemNote: TypeNotes, holder: NoteViewHolder) {
        displayTitleTask(itemNote, holder)
        createSettingsButton(holder, itemNote)
        holder.binding.root.setOnLongClickListener {
                changeFinished(itemNote)
                true
        }
    }

    private fun showProduct(itemNote: TypeNotes, holder: ProductViewHolder) {
        with(holder.binding) {
            displayTitleTask(itemNote, holder)
            recyclerListProducts.adapter =
                ProductsListAdapter(
                    onLongClickChangeOneProductFinished = onClickChangeFinishedProduct,
                    products = (itemNote as Products)
                )
            createSettingsButton(holder, itemNote)

            titleTask.setOnLongClickListener {
                changeFinished(itemNote)
                true
            }
        }
    }

    private fun showReminder(itemNote: TypeNotes, holder: ReminderViewHolder) {
        with(holder.binding) {
            displayTitleTask(itemNote, holder)
            time.text = String.format(
                "%02d:%02d",
                (itemNote as Reminder).time.hour,
                itemNote.time.minute
            )
            createSettingsButton(holder, itemNote)

            root.setOnLongClickListener {
                changeFinished(itemNote)
                true
            }
        }
    }

    private fun showMedication(itemNote: TypeNotes, holder: MedicationViewHolder) {
        with(holder.binding) {
            displayTitleTask(itemNote, holder)
            time.text = String.format(
                "%02d:%02d",
                (itemNote as Medications).time.hour,
                itemNote.time.minute
            )
            createSettingsButton(holder, itemNote)

            root.setOnLongClickListener {
                changeFinished(itemNote)
                true
            }
        }
    }

    private fun displayTitleTask(itemNote: TypeNotes, holder: BaseViewHolder) {
        when (holder) {
            is MedicationViewHolder -> {
                if (!itemNote.finished) {
                    holder.binding.titleTask.text = itemNote.title
                } else {
                    holder.binding.titleTask.text = createSpanned(itemNote)
                }
            }

            is NoteViewHolder -> {
                if (!itemNote.finished) {
                    holder.binding.titleTask.text = itemNote.title
                } else {
                    holder.binding.titleTask.text = createSpanned(itemNote)
                }
            }

            is ProductViewHolder -> {
                if (!itemNote.finished) {
                    holder.binding.titleTask.text = itemNote.title
                } else {
                    holder.binding.titleTask.text = createSpanned(itemNote)
                }
            }

            is ReminderViewHolder -> {
                if (!itemNote.finished) {
                    holder.binding.titleTask.text = itemNote.title
                } else {
                    holder.binding.titleTask.text = createSpanned(itemNote)
                }
            }
        }
    }

    private fun createSpanned(itemNote: TypeNotes): Spanned {
        return HtmlCompat.fromHtml(
            ("<strike>" + itemNote.title + "</strike>"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun changeFinished(itemNote: TypeNotes) {
        onClickChangeFinishedNote(itemNote)
    }

    private fun createSettingsButton(holder: BaseViewHolder, itemNote: TypeNotes) {
        when (holder) {
            is NoteViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemNote)
                }
            }

            is MedicationViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemNote)
                }
            }

            is ProductViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemNote)
                }
            }

            is ReminderViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemNote)
                }
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, itemNote: TypeNotes) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.change_note -> {
                    onChangeNote(itemNote)
                }
                R.id.delete_note -> {
                    onDeleteNote(itemNote)
                }
            }
            false
        }
        popup.show()
    }

    companion object {
        private const val TYPE_NOTE = 0
        private const val TYPE_PRODUCTS = 1
        private const val TYPE_REMINDER = 2
        private const val TYPE_MEDICATION = 3
    }
}

sealed class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class NoteViewHolder(val binding: ItemTaskNoteBinding) : BaseViewHolder(binding.root)

class ProductViewHolder(val binding: ItemTaskProductsBinding) : BaseViewHolder(binding.root)

class ReminderViewHolder(val binding: ItemTaskReminderBinding) : BaseViewHolder(binding.root)

class MedicationViewHolder(val binding: ItemTaskMedicationBinding) : BaseViewHolder(binding.root)

