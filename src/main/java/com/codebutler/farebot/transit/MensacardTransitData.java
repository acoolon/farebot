/*
 * MensacardTransitData.java
 *
 * Authors: github.com/acoolon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.farebot.transit;

import android.os.Parcel;
import com.codebutler.farebot.ListItem;
import com.codebutler.farebot.Utils;
import com.codebutler.farebot.card.Card;
import com.codebutler.farebot.card.desfire.DesfireCard;
import com.codebutler.farebot.card.desfire.DesfireFile;
import com.codebutler.farebot.card.desfire.DesfireFile.ValueDesfireFile;
import com.codebutler.farebot.card.desfire.DesfireApplication;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

public class MensacardTransitData extends TransitData {
    private static final int CREDIT_APPLICATION    = 0x5f8415;

    private int     mBalance;

    public static boolean check (Card card) {
        if (card instanceof DesfireCard) {
            DesfireCard c = (DesfireCard) card;
            DesfireApplication app = c.getApplication(CREDIT_APPLICATION);
            if (app != null) {
                return (app.getFile(0x1) instanceof ValueDesfireFile);
            }
        }
        return false;
    }

    public static TransitIdentity parseTransitIdentity(Card card) {
        // Maybe 0x5f804 file 0x7
        return new TransitIdentity("Mencacard", null);
    }

    public MensacardTransitData (Parcel parcel) {
        mBalance      = parcel.readInt();
    }
    
    public MensacardTransitData (Card card) {
        DesfireCard desfireCard = (DesfireCard) card;
        ValueDesfireFile file = (ValueDesfireFile) desfireCard.getApplication(CREDIT_APPLICATION).getFile(0x1);
        mBalance = file.getValue();
    }

    @Override
    public String getCardName () {
        return "Mensacard";
    }

    @Override
    public String getBalanceString () {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY).format((double) mBalance / 1000);
    }

    @Override
    public String getSerialNumber () {
        // XXX
        return "";
    }

    @Override
    public Trip[] getTrips () {
        return null;
    }

    @Override
    public Refill[] getRefills () {
        return null;
    }

    @Override
    public Subscription[] getSubscriptions() {
        return null;
    }

    @Override
    public List<ListItem> getInfo() {
        return null;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mBalance);
    }
}
