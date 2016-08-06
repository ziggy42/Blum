package com.andreapivetta.blu.ui.explore

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andreapivetta.blu.R

/**
 * Created by andrea on 28/07/16.
 */
class ExploreFragment : Fragment(), ExploreMvpView {

    companion object {
        fun newInstance() = ExploreFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_explore, container, false)
        return rootView
    }

}