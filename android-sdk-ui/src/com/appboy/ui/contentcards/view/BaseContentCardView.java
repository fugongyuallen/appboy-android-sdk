package com.appboy.ui.contentcards.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appboy.models.cards.Card;
import com.appboy.ui.actions.IAction;
import com.appboy.ui.actions.UriAction;
import com.appboy.ui.contentcards.AppboyContentCardsManager;
import com.appboy.ui.widget.BaseCardView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Base class for Appboy ContentCard views
 */
public abstract class BaseContentCardView<T extends Card> extends BaseCardView<T> {
  public BaseContentCardView(Context context) {
    super(context);
  }

  public abstract ContentCardViewHolder createViewHolder(ViewGroup viewGroup);

  public void bindViewHolder(ContentCardViewHolder viewHolder, final T card) {
    viewHolder.setPinnedIconVisible(card.getIsPinned());
    viewHolder.setUnreadBarVisible(mAppboyConfigurationProvider.isContentCardsUnreadVisualIndicatorEnabled() && !card.isRead());
    final UriAction mCardAction = getUriActionForCard(card);
    viewHolder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        handleCardClick(mContext, card, mCardAction, getClassLogTag());
      }
    });
  }

  /**
   * Sets the card's image to a given url based on whether Fresco is enabled. One or both of the views may be null.
   *
   * @param imageView The ImageView
   * @param simpleDraweeView The Fresco Drawee view
   * @param cardAspectRatio The aspect ratio as set by the card itself
   * @param cardImageUrl The image url
   * @param defaultAspectRatio The default aspect ratio if the cardAspectRatio is 0
   */
  public void setOptionalCardImage(@Nullable ImageView imageView, @Nullable SimpleDraweeView simpleDraweeView, float cardAspectRatio,
                                   String cardImageUrl, float defaultAspectRatio) {
    boolean respectAspectRatio = false;
    float aspectRatio = defaultAspectRatio;

    if (cardAspectRatio != 0f) {
      aspectRatio = cardAspectRatio;
      respectAspectRatio = true;
    }

    if (canUseFresco()) {
      if (simpleDraweeView != null) {
        setSimpleDraweeToUrl(simpleDraweeView, cardImageUrl, aspectRatio, respectAspectRatio);
      }
    } else {
      if (imageView != null) {
        setImageViewToUrl(imageView, cardImageUrl, aspectRatio, respectAspectRatio);
      }
    }
  }

  @Override
  protected boolean isClickHandled(Context context, Card card, IAction cardAction) {
    return AppboyContentCardsManager.getInstance().getContentCardsActionListener().onContentCardClicked(context, card, cardAction);
  }
}
