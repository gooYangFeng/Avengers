/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package saulmm.avengers.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import saulmm.avengers.AvengersApplication;
import saulmm.avengers.R;
import saulmm.avengers.injector.components.DaggerAvengersComponent;
import saulmm.avengers.injector.modules.ActivityModule;
import saulmm.avengers.injector.modules.AvengersModule;
import saulmm.avengers.model.Character;
import saulmm.avengers.mvp.presenters.AvengersListPresenter;
import saulmm.avengers.mvp.views.AvengersView;
import saulmm.avengers.views.adapter.AvengersListAdapter;


public class AvengersListActivity extends AppCompatActivity
    implements AvengersView {

    public final static String EXTRA_CHARACTER_ID = "character_id";

    @Bind(R.id.activity_avengers_recycler) RecyclerView mAvengersRecycler;
    @Bind(R.id.activity_avengers_toolbar) Toolbar mAvengersToolbar;
    @Inject AvengersListPresenter mAvengersListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avengers_list);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeRecyclerView();
        initializeDependencyInjector();
        initializePresenter();
    }

    private void initializeToolbar() {
        setSupportActionBar(mAvengersToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAvengersListPresenter.onStart();
    }

    private void initializePresenter() {
        mAvengersListPresenter.attachView(this);
    }

    private void initializeDependencyInjector() {
        AvengersApplication avengersApplication = (AvengersApplication) getApplication();

        DaggerAvengersComponent.builder()
            .avengersModule(new AvengersModule())
            .activityModule(new ActivityModule(this))
            .appComponent(avengersApplication.getAppComponent())
            .build().inject(this);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAvengersRecycler.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void showAvengersList(List<Character> avengers) {
        AvengersListAdapter avengersListAdapter = new AvengersListAdapter(avengers, this, mAvengersListPresenter);
        mAvengersRecycler.setAdapter(avengersListAdapter);
    }

    @Override
    protected void onStop() {

        super.onStop();
        mAvengersListPresenter.onStop();
    }
}
