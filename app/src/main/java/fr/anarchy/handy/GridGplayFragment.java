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
import android.os.Handler;
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

import fr.anarchy.handy.fr.anarchy.handy.json.GlobalJSON;
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
    ArrayList<Card> cards;
    CardGridView listView;
    CardGridArrayAdapter mCardArrayAdapter;
    MainActivity main;

    Handler myHandler;


    public int getTitleResourceId() {
        return R.string.carddemo_title_grid_gplay;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_grid_card_view, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myHandler = new Handler();

        pokemonVectorImage = new int[151];

        String packageName = getActivity().getPackageName();

        // load images in array
        for (int imageIndex = 0; imageIndex < pokemonVectorImage.length; imageIndex++) {
            resId = getActivity().getResources().getIdentifier("pokemon_vector_" + (imageIndex + 1), "drawable", packageName);
            pokemonVectorImage[imageIndex] = resId;
        }


          // initialize cards with no content except images
            cards = new ArrayList<Card>();
            for (int i = 0; i < 151; i++) {
                cards.add(initCard(i, "................."));
            }


        updateNames();

        updateGrid();
    }

    // method used to create a card
    public GplayGridCard initCard(int i, String name) {

        Log.v("name", name);
        // initialize the card
        GplayGridCard card = new GplayGridCard(getActivity());
        // with mutliple parameters
        card.headerTitle = "#" + String.format("%03d", i + 1);
        card.secondaryTitle = name;
        card.type1title = "lol";
        card.firstTypeID = 1;
        card.type2title = "mdr";
        card.resourceIdThumbnail = pokemonVectorImage[i];

        card.init();

        return card;
    }

    // call this method always after cards have been initialized
    public void updateGrid() {
        // the adapter will get the latest cards state
        mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);

        listView = (CardGridView) getActivity().findViewById(R.id.carddemo_grid_base1);
        if (listView != null) {
            // p
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    // this method calls the other necessary methods in order to update the grid correctly
    public void changeCardsName(int index) {
        GplayGridCard card = initCard(index, GlobalJSON.pokemonNames[index]);
        cards.set(index, card);
        updateGrid();
    }

    public void updateNames(){
        this.myHandler.post(new Runnable() {

            public void run() {
                for (int i = 0; i < 151; i++) {
                    changeCardsName(i);
                }
            }
        });
    }


    /*******************************************************************/

    // card class
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
        public TextView type2;
        public TextView subtitle;

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

            TextView title = (TextView) view.findViewById(R.id.handy_card_header_inner_simple_title);
            title.setText(headerTitle);

            subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);

            TextView type1 = (TextView) view.findViewById(R.id.home_card_type);
            type1.setText(type1title);

            type2 = (TextView) view.findViewById(R.id.home_card_type_2);
            type2.setText(type2title);

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
