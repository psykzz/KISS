package fr.neamar.kiss.dataprovider;

import android.database.ContentObserver;
import android.provider.CallLog;

import fr.neamar.kiss.forwarder.Permission;
import fr.neamar.kiss.loader.LoadCallLogPojos;
import fr.neamar.kiss.normalizer.StringNormalizer;
import fr.neamar.kiss.pojo.CallLogPojo;
import fr.neamar.kiss.searcher.Searcher;
import fr.neamar.kiss.utils.FuzzyScore;

public class CalllogProvider extends Provider<CallLogPojo> {

    private final ContentObserver cObserver = new ContentObserver(null) {

        @Override
        public void onChange(boolean selfChange) {
            //reload contacts
            reload();
        }
    };

    @Override
    public void reload() {
        super.reload();
        this.initialize(new LoadCallLogPojos(this));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // register content observer if we have permission
        if(Permission.checkCallLogPermission(this)) {
            getContentResolver().registerContentObserver(CallLog.CONTENT_URI, false, cObserver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //deregister content observer
        getContentResolver().unregisterContentObserver(cObserver);
    }

    @Override
    public void requestResults(String query, Searcher searcher) {
        StringNormalizer.Result queryNormalized = StringNormalizer.normalizeWithResult(query, false);

        if (queryNormalized.codePoints.length == 0) {
            return;
        }

        // Require a certain length
        if (queryNormalized.length() <= 2) {
            return;
        }

        FuzzyScore fuzzyScore = new FuzzyScore(queryNormalized.codePoints);
        FuzzyScore.MatchInfo matchInfo = new FuzzyScore.MatchInfo();
        for (CallLogPojo pojo : pojos) {
            boolean match = fuzzyScore.match(pojo.normalizedName.codePoints, matchInfo);
            pojo.relevance = matchInfo.score;

            if (!pojo.number.isEmpty()) {
                if (fuzzyScore.match(pojo.number, matchInfo)) {
                    if (!match || (matchInfo.score > pojo.relevance)) {
                        match = true;
                        pojo.relevance = matchInfo.score;
                        pojo.clearNameHighlight();
                    }
                }
            }

            if (match) {
                if (!searcher.addResult(pojo))
                    return;
            }
        }
    }
}
