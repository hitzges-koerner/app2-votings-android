package appsquared.votings.app.tag.interfaces;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 3/18/2019 at 7:18 PM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 3/18/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


import appsquared.votings.app.tag.model.TagModel;

public interface TagItemListener {
    void onGetAddedItem(TagModel tagModel);

    void onGetRemovedItem(TagModel model);
}
