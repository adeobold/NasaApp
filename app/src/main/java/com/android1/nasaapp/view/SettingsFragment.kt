package com.android1.nasaapp.view

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android1.nasaapp.R
import com.android1.nasaapp.databinding.FragmentSettingsBinding
import com.google.android.material.chip.Chip

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

        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->

            when (chipGroup.findViewById<Chip>(position)?.text) {
                resources.getString(R.string.ThemeRoundText) ->
                    activity?.theme?.applyStyle(R.style.IndigoTheme, true)
                resources.getString(R.string.ThemeSquareText) ->
                    activity?.theme?.applyStyle(R.style.PinkTheme, true)
            }
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