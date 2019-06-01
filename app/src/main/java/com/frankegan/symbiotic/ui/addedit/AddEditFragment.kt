package com.frankegan.symbiotic.ui.addedit


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.injector
import kotlinx.android.synthetic.main.add_edit_fragment.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditFragment : Fragment() {

    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireActivity,
        factoryProducer = { factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fermentationId = AddEditFragmentArgs.fromBundle(arguments!!).fermentationId
        viewModel.loadFermentationData(fermentationId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /////////////////
        //FAB Actions
        /////////////////
        fab.setOnClickListener {
            viewModel.saveFermentation()
            findNavController().navigate(AddEditFragmentDirections.homeAction())
        }
        /////////////////
        //Setup Tabs and ViewPager
        /////////////////
        val titles = listOf("Details", "Ingredients", "Gallery")
        view_pager.adapter =
            object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int) = when (position) {
                    0 -> DetailsFragment.newInstance()
                    1 -> IngredientsFragment.newInstance()
                    2 -> GalleryFragment.newInstance()
                    else -> throw Error("Impossible state in ViewPager")
                }

                override fun getCount() = titles.size

                override fun getPageTitle(position: Int) = titles[position]
            }
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("white"))
    }
}
