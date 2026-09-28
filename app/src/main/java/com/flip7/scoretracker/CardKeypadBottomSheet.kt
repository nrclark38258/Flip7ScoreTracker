package com.flip7.scoretracker

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors

class CardKeypadBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val MAX_NUMBER_CARDS = 7
    }

    var playerName: String = ""
    var initialSelection: CardSelection? = null
    var initialManualOverride: Int? = null
    var onConfirm: ((Int, CardSelection?, Int?) -> Unit)? = null

    private val selectedNumbers = mutableSetOf<Int>()
    private val selectedModifiers = mutableSetOf<ModifierCard>()

    private lateinit var totalText: TextView
    private lateinit var bonusText: TextView
    private lateinit var manualScoreInput: EditText
    private lateinit var numberButtons: Map<Int, MaterialButton>
    private lateinit var modifierButtons: Map<ModifierCard, MaterialButton>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_card_keypad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.playerNameHeader).text = playerName
        totalText = view.findViewById(R.id.totalDisplayText)
        bonusText = view.findViewById(R.id.flip7BonusText)
        manualScoreInput = view.findViewById(R.id.manualScoreInput)

        numberButtons = mapOf(
            0 to view.findViewById<MaterialButton>(R.id.cardNum0),
            1 to view.findViewById<MaterialButton>(R.id.cardNum1),
            2 to view.findViewById<MaterialButton>(R.id.cardNum2),
            3 to view.findViewById<MaterialButton>(R.id.cardNum3),
            4 to view.findViewById<MaterialButton>(R.id.cardNum4),
            5 to view.findViewById<MaterialButton>(R.id.cardNum5),
            6 to view.findViewById<MaterialButton>(R.id.cardNum6),
            7 to view.findViewById<MaterialButton>(R.id.cardNum7),
            8 to view.findViewById<MaterialButton>(R.id.cardNum8),
            9 to view.findViewById<MaterialButton>(R.id.cardNum9),
            10 to view.findViewById<MaterialButton>(R.id.cardNum10),
            11 to view.findViewById<MaterialButton>(R.id.cardNum11),
            12 to view.findViewById<MaterialButton>(R.id.cardNum12)
        )
        modifierButtons = mapOf(
            ModifierCard.PLUS_2 to view.findViewById(R.id.modPlus2),
            ModifierCard.PLUS_4 to view.findViewById(R.id.modPlus4),
            ModifierCard.PLUS_6 to view.findViewById(R.id.modPlus6),
            ModifierCard.PLUS_8 to view.findViewById(R.id.modPlus8),
            ModifierCard.PLUS_10 to view.findViewById(R.id.modPlus10),
            ModifierCard.X2 to view.findViewById(R.id.modX2)
        )

        numberButtons.forEach { (number, button) ->
            button.setOnClickListener { toggleNumber(number) }
        }
        modifierButtons.forEach { (modifier, button) ->
            button.setOnClickListener { toggleModifier(modifier) }
        }

        manualScoreInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateTotalDisplay()
            }
        })

        view.findViewById<View>(R.id.clearButton).setOnClickListener { clearAll() }
        view.findViewById<View>(R.id.doneButton).setOnClickListener { confirmAndDismiss() }

        applyInitialState()
    }

    private fun applyInitialState() {
        val selection = initialSelection
        if (selection != null) {
            selectedNumbers.addAll(selection.numbers)
            selectedModifiers.addAll(selection.modifiers)
        }
        initialManualOverride?.let { manualScoreInput.setText(it.toString()) }
        refreshButtonStates()
        updateTotalDisplay()
    }

    private fun toggleNumber(number: Int) {
        if (!selectedNumbers.remove(number)) {
            if (selectedNumbers.size >= MAX_NUMBER_CARDS) {
                return
            }
            selectedNumbers.add(number)
        }
        refreshButtonStates()
        updateTotalDisplay()
    }

    private fun toggleModifier(modifier: ModifierCard) {
        if (!selectedModifiers.remove(modifier)) {
            selectedModifiers.add(modifier)
        }
        refreshButtonStates()
        updateTotalDisplay()
    }

    private fun refreshButtonStates() {
        val unselectedBackground = MaterialColors.getColor(requireView(), android.R.attr.colorBackground)
        val unselectedTextColor = MaterialColors.getColor(requireView(), android.R.attr.textColorPrimary)
        val numberColor = resources.getColor(R.color.card_number_color, requireContext().theme)
        val modifierColor = resources.getColor(R.color.card_modifier_color, requireContext().theme)
        val selectedTextColor = resources.getColor(R.color.white, requireContext().theme)

        val atCap = selectedNumbers.size >= MAX_NUMBER_CARDS
        numberButtons.forEach { (number, button) ->
            val selected = number in selectedNumbers
            button.backgroundTintList = ColorStateList.valueOf(if (selected) numberColor else unselectedBackground)
            button.setTextColor(if (selected) selectedTextColor else unselectedTextColor)
            button.isEnabled = selected || !atCap
            button.alpha = if (button.isEnabled) 1f else 0.4f
        }
        modifierButtons.forEach { (modifier, button) ->
            val selected = modifier in selectedModifiers
            button.backgroundTintList = ColorStateList.valueOf(if (selected) modifierColor else unselectedBackground)
            button.setTextColor(if (selected) selectedTextColor else unselectedTextColor)
        }
    }

    private fun clearAll() {
        selectedNumbers.clear()
        selectedModifiers.clear()
        manualScoreInput.setText("")
        refreshButtonStates()
        updateTotalDisplay()
    }

    private fun currentSelection() = CardSelection(selectedNumbers.toSet(), selectedModifiers.toSet())

    private fun manualValue(): Int? = manualScoreInput.text?.toString()?.toIntOrNull()

    private fun updateTotalDisplay() {
        val total = manualValue() ?: currentSelection().computeTotal()
        totalText.text = getString(R.string.keypad_total_label, total)
        bonusText.visibility = if (selectedNumbers.size == MAX_NUMBER_CARDS) View.VISIBLE else View.GONE
    }

    private fun confirmAndDismiss() {
        val selection = currentSelection()
        val selectionToReturn = if (selection.numbers.isEmpty() && selection.modifiers.isEmpty()) null else selection
        val manualOverride = manualValue()
        val total = manualOverride ?: selection.computeTotal()
        onConfirm?.invoke(total, selectionToReturn, manualOverride)
        dismiss()
    }
}
