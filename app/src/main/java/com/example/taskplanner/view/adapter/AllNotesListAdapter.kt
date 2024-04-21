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
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.databinding.ItemTaskMedicationBinding
import com.example.taskplanner.databinding.ItemTaskNoteBinding
import com.example.taskplanner.databinding.ItemTaskProductsBinding
import com.example.taskplanner.databinding.ItemTaskReminderBinding

class AllNotesListAdapter(
    private val onDeleteTask: (TypeTask) -> Unit,
    private val onClickChangeFinishedProduct: (Product) -> Unit,
    private val onClickChangeFinishedTask: (TypeTask) -> Unit,
    private val onChangeTask: (TypeTask) -> Unit,
    private var day: Day
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
            is ProductsWithList -> TYPE_PRODUCTS
            is Reminder -> TYPE_REMINDER
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemTask = day.list[position]

        when (holder) {
            is NoteViewHolder -> {
                showNote(itemTask, holder)
            }

            is ProductViewHolder -> {
                showProduct(itemTask as ProductsWithList, holder)
            }

            is ReminderViewHolder -> {
                showReminder(itemTask, holder)
            }

            is MedicationViewHolder -> {
                showMedication(itemTask, holder)
            }
        }
    }

    private fun showNote(note: TypeTask, holder: NoteViewHolder) {
        displayTitleTask(note, holder)
        createSettingsButton(holder, note)
        holder.binding.root.setOnLongClickListener {
            changeFinished(note)
            true
        }
    }

    private fun showProduct(products: ProductsWithList, holder: ProductViewHolder) {
        with(holder.binding) {
            displayTitleTask(products, holder)
            if (products.listProducts != null) {
                recyclerListProducts.adapter =
                    ProductsListAdapter(
                        onLongClickChangeOneProductFinished = onClickChangeFinishedProduct,
                        product = products.listProducts
                    )
            }
            createSettingsButton(holder, products)

            titleTask.setOnLongClickListener {
                changeFinished(products)
                true
            }
        }
    }

    private fun showReminder(reminder: TypeTask, holder: ReminderViewHolder) {
        with(holder.binding) {
            displayTitleTask(reminder, holder)
            time.text = String.format(
                "%02d:%02d",
                (reminder as Reminder).time.hour,
                reminder.time.minute
            )
            createSettingsButton(holder, reminder)

            root.setOnLongClickListener {
                changeFinished(reminder)
                true
            }
        }
    }

    private fun showMedication(medication: TypeTask, holder: MedicationViewHolder) {
        with(holder.binding) {
            displayTitleTask(medication, holder)
            time.text = String.format(
                "%02d:%02d",
                (medication as Medications).time.hour,
                medication.time.minute
            )
            createSettingsButton(holder, medication)

            root.setOnLongClickListener {
                changeFinished(medication)
                true
            }
        }
    }

    private fun displayTitleTask(task: TypeTask, holder: BaseViewHolder) {
        when (holder) {
            is MedicationViewHolder -> {
                if (!task.finished) {
                    holder.binding.titleTask.text = task.title
                } else {
                    holder.binding.titleTask.text = createSpanned(task)
                }
            }

            is NoteViewHolder -> {
                if (!task.finished) {
                    holder.binding.titleTask.text = task.title
                } else {
                    holder.binding.titleTask.text = createSpanned(task)
                }
            }

            is ProductViewHolder -> {
                if (!task.finished) {
                    holder.binding.titleTask.text = task.title
                } else {
                    holder.binding.titleTask.text = createSpanned(task)
                }
            }

            is ReminderViewHolder -> {
                if (!task.finished) {
                    holder.binding.titleTask.text = task.title
                } else {
                    holder.binding.titleTask.text = createSpanned(task)
                }
            }
        }
    }

    private fun createSpanned(itemTask: TypeTask): Spanned {
        return HtmlCompat.fromHtml(
            ("<strike>" + itemTask.title + "</strike>"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun changeFinished(itemTask: TypeTask) {
        onClickChangeFinishedTask(itemTask)
    }

    private fun createSettingsButton(holder: BaseViewHolder, itemTask: TypeTask) {
        when (holder) {
            is NoteViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemTask)
                }
            }

            is MedicationViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemTask)
                }
            }

            is ProductViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemTask)
                }
            }

            is ReminderViewHolder -> {
                holder.binding.buttonSetting.setOnClickListener {
                    showMenu(it, R.menu.menu_button_settings_item_note, itemTask)
                }
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, itemTask: TypeTask) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.change_note -> {
                    onChangeTask(itemTask)
                }

                R.id.delete_note -> {
                    onDeleteTask(itemTask)
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
