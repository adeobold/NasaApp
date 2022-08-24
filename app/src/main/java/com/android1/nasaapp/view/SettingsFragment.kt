package com.android1.nasaapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.android1.nasaapp.R
import com.android1.nasaapp.databinding.FragmentSettingsBinding
import com.android1.nasaapp.utils.SETTINGS_THEME
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity?.intent?.getStringExtra(SETTINGS_THEME).toString()==resources.getString(R.string.ThemeRoundText)){
            activity?.findViewById<Chip>(R.id.chipRoundTheme)?.isChecked = true
        }else{
            activity?.findViewById<Chip>(R.id.chipSquareTheme)?.isChecked = true
        }

        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            when (chipGroup.findViewById<Chip>(position)?.text) {
                resources.getString(R.string.ThemeRoundText) ->
                    context?.theme?.applyStyle(R.style.RoundTheme, true)
                resources.getString(R.string.ThemeSquareText) ->
                    context?.theme?.applyStyle(R.style.SquareTheme, true)
            }
            activity?.intent?.putExtra(SETTINGS_THEME, chipGroup.findViewById<Chip>(position)?.text)
            activity?.intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.recreate()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}