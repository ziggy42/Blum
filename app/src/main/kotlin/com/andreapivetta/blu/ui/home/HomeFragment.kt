package com.andreapivetta.blu.ui.home

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.*
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.search.SearchActivity
import com.andreapivetta.blu.ui.settings.SettingsActivity
import com.andreapivetta.blu.ui.timeline.TimelineFragment

/**
 * Created by andrea on 27/09/16.
 */
class HomeFragment : TimelineFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)

        val searchView = MenuItemCompat.
                getActionView(menu?.findItem(R.id.action_search)) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                search(searchView.query.toString())
                return true
            }

            override fun onQueryTextChange(s: String) = false
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> openSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    fun search(string: String) {
        SearchActivity.launch(context, string)
    }

    fun openSettings() {
        SettingsActivity.launch(context)
    }

}