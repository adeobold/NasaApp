package com.android1.nasaapp.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.android1.nasaapp.MainActivity
import com.android1.nasaapp.R
import com.android1.nasaapp.databinding.FragmentPictureOfTheDayBinding
import com.android1.nasaapp.utils.DATE_FORMAT
import com.android1.nasaapp.utils.SETTINGS_THEME
import com.android1.nasaapp.viewmodel.PictureOfTheDayViewModel
import com.geekbrains.materialyou.ui.picture.BottomNavigationDrawerFragment
import com.geekbrains.materialyou.ui.picture.PictureOfTheDayData
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val startDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT))
        viewModel.getData(startDate)
            .observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                var startDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                when(it.text){
                    getResources().getString(R.string.chipTwoDaysBefore) ->
                        startDate = LocalDateTime.now().minusDays(2)
                        .format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                    getResources().getString(R.string.chipYesterdayText) ->
                        startDate = LocalDateTime.now().minusDays(1)
                        .format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                    getResources().getString(R.string.chipTodayText) ->
                        startDate = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                }
                viewModel.getData(startDate).observe(viewLifecycleOwner) { renderData(it) }
            }
        }

        setBottomAppBar(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container, SettingsFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData

                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        crossfade(true)
                    }
                }

                serverResponseData.explanation.let { explanationText ->
                    val bottomSheetDescription: TextView? = view?.findViewById(R.id.bottomSheetDescription)
                    if (bottomSheetDescription != null) {
                        bottomSheetDescription.text = explanationText
                    }
                }

                serverResponseData.title.let { titleText ->
                    val bottomSheetDescriptionHeader: TextView? = view?.findViewById(R.id.bottomSheetDescriptionHeader)
                    if (bottomSheetDescriptionHeader != null) {
                        bottomSheetDescriptionHeader.text = titleText
                    }
                }

            }
            is PictureOfTheDayData.Loading -> {
                //nothing to do
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet : ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.peekHeight = 500
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}