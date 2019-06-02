package com.frankegan.symbiotic.ui.addedit


import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.injector
import kotlinx.android.synthetic.main.add_edit_fragment.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditFragment : Fragment() {

    /**
     * Safe args from Navigation Manager.
     */
    private val safeArgs by navArgs<AddEditFragmentArgs>()

    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireActivity,
        factoryProducer = { factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadFermentationData(safeArgs.fermentationId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.add_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /////////////////
        //Setup Tabs and ViewPager
        /////////////////
        val titles = resources.getStringArray(R.array.add_edit)
        view_pager.adapter =
            object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getCount() = titles.size
                override fun getPageTitle(position: Int) = titles[position]
                override fun getItem(position: Int) = when (position) {
                    0 -> DetailsFragment.newInstance()
                    1 -> IngredientsFragment.newInstance()
                    2 -> GalleryFragment.newInstance()
                    else -> throw Error("Impossible state in ViewPager")
                }
            }
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("white"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_data -> {
                viewModel.saveFermentation()
                findNavController().navigate(AddEditFragmentDirections.homeAction())
            }
            R.id.delete_data -> {
                viewModel.deleteFermentation()
                findNavController().navigate(AddEditFragmentDirections.homeAction())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
