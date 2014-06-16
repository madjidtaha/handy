/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package fr.anarchy.handy;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Grid as Google Play example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GridGplayFragment extends BaseFragment {

    protected ScrollView mScrollView;

    public PokemonDatabase pokemonDB;

    private Cursor pokemonNames;
    private Cursor pokemonTypesNames;
    private Cursor pokemonTypesNames2;

    int resId;

    int[] pokemonVectorImage;

    int localLanguageID = 9;


    @Override
    public int getTitleResourceId() {
        return R.string.carddemo_title_grid_gplay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_grid_card_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pokemonDB = new PokemonDatabase(getActivity());

        pokemonVectorImage = new int[151];

        String packageName = getActivity().getPackageName();

        for (int imageIndex = 0; imageIndex < pokemonVectorImage.length; imageIndex++) {
            resId = getActivity().getResources().getIdentifier("pokemon_vector_" + (imageIndex + 1), "drawable", packageName);
            pokemonVectorImage[imageIndex] = resId;

            Log.v("image", imageIndex + "");
        }


        initCards();
    }


/*    private void initCards() {
        new Thread(
                new Runnable() {
                    public void run() {
                        initCardsThread();
                    }
                }
        ).start();
    }*/


    private void initCards() {


        String[] sqlSelect = {"name"};
        String selector = "local_language_id = " + localLanguageID;
        String sqlTables = "pokemon_species_names";
        pokemonNames = pokemonDB.getPokemonInfos(sqlSelect, selector, sqlTables);

        pokemonTypesNames = pokemonDB.rawQuery(
                "SELECT type_names.name, type_names.type_id" +
                        " FROM type_names JOIN pokemon_types" +
                        " ON pokemon_types.type_id = type_names.type_id" +
                        " AND pokemon_types.slot = 1" +
                        " AND type_names.local_language_id = " + localLanguageID
        );

        pokemonTypesNames2 = pokemonDB.rawQuery(
                "SELECT type_names.name" +
                        " FROM type_names JOIN pokemon_types" +
                        " ON pokemon_types.type_id = type_names.type_id" +
                        " AND pokemon_types.slot = 2" +
                        " AND type_names.local_language_id = " + localLanguageID
        );


        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 151; i++) {

            pokemonNames.moveToPosition(i);

            GplayGridCard card = new GplayGridCard(getActivity());

            card.headerTitle = "#" + String.format("%03d", i + 1);


            card.secondaryTitle = pokemonNames.getString(0);

            pokemonTypesNames.moveToPosition(i);
            pokemonTypesNames2.moveToPosition(i);

            card.type1title = pokemonTypesNames.getString(0);
            card.firstTypeID = Integer.parseInt(pokemonTypesNames.getString(1));

            card.type2title = pokemonTypesNames2.getString(0);

            card.rating = 0;

            card.resourceIdThumbnail = pokemonVectorImage[i];

            card.init();
            cards.add(card);
        }

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);

        CardGridView listView = (CardGridView) getActivity().findViewById(R.id.carddemo_grid_base1);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }


    }


    public class GplayGridCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;
        protected String headerTitle;
        protected String secondaryTitle;
        protected String type1title;
        protected int firstTypeID;
        protected String type2title;
        protected float rating;


        public GplayGridCard(Context context) {
            super(context, R.layout.home_card_inner_content);
        }

        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            addCardHeader(header);

            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
            if (resourceIdThumbnail > -1)
                thumbnail.setDrawableResource(resourceIdThumbnail);
            else
                thumbnail.setDrawableResource(R.drawable.pokemon_vector_6);
            addCardThumbnail(thumbnail);

            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

           /* TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
            title.setText("FREE");*/

            TextView title = (TextView) view.findViewById(R.id.handy_card_header_inner_simple_title);
            title.setText(headerTitle);

            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);

            TextView type1 = (TextView) view.findViewById(R.id.home_card_type);
            type1.setText(type1title);

            TextView type2 = (TextView) view.findViewById(R.id.home_card_type_2);
            type1.setText(type2title);

            GradientDrawable shape = (GradientDrawable) type1.getBackground();


            switch (firstTypeID) {
                case 1:
                    shape.setColor(0xffA9A878);
                    break;
                case 2:
                    shape.setColor(0xffF07F2F);
                    break;
                case 3:
                    shape.setColor(0xffA890F0);
                    break;
                case 4:
                    shape.setColor(0xffA041A1);
                    break;
                case 5:
                    shape.setColor(0xffDFBF68);
                    break;
                case 6:
                    shape.setColor(0xffB89F38);
                    break;
                case 7:
                    shape.setColor(0xffA8B821);
                    break;
                case 8:
                    shape.setColor(0xff715899);
                    break;
                case 9:
                    shape.setColor(0xffB8B8D0);
                    break;
                case 10:
                    shape.setColor(0xffed1600);
                    break;
                case 11:
                    shape.setColor(0xff6690EF);
                    break;
                case 12:
                    shape.setColor(0xff78C750);
                    break;
                case 13:
                    shape.setColor(0xffF7D02F);
                    break;
                case 14:
                    shape.setColor(0xffF65887);
                    break;
                case 15:
                    shape.setColor(0xff98D8D8);
                    break;
                case 16:
                    shape.setColor(0xff6F38F8);
                    break;
                case 17:
                    shape.setColor(0xff6F5848);
                    break;
                case 18:
                    shape.setColor(0xffB8B8D0);
                    break;
                default:
                    shape.setColor(0xffB8B8D0);
                    break;
            }

 /*           RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);

            mRatingBar.setNumStars(5);
            mRatingBar.setMax(5);
            mRatingBar.setStepSize(0.5f);
            mRatingBar.setRating(rating);*/
        }

        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                //viewImage.getLayoutParams().width = 196;
                //viewImage.getLayoutParams().height = 196;

            }
        }

    }

}
