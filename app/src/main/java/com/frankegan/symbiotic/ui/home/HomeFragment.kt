package com.frankegan.symbiotic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Fermentation
import kotlinx.android.synthetic.main.home_fragment.*
import org.threeten.bp.LocalDateTime

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addEditAction(fermentationId = null))
        }
        recycler_view.adapter = FermentationAdapter().apply {
            updateItems(
                listOf(
                    Fermentation(
                        title = "Razz",
                        startDate = LocalDateTime.now().minusWeeks(1),
                        firstEndDate = LocalDateTime.now().plusWeeks(1),
                        secondEndDate = LocalDateTime.now().plusWeeks(1).plusDays(4)
                    )
                )
            )
        }
    }


    companion object {
        fun newInstance() = HomeFragment()
    }
}
