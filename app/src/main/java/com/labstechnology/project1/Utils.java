package com.labstechnology.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.Announcement;
import com.labstechnology.project1.models.Assignment;
import com.labstechnology.project1.models.MultipleChoiceQuestion;
import com.labstechnology.project1.models.Quiz;
import com.labstechnology.project1.models.QuizScore;
import com.labstechnology.project1.models.User;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Utils {
    private static final String TAG = "Utils";
    private static int quizId;
    private static int assignmentId;
    private static int quizScoreId;
    private static int assignmentScoreId;
    private static int eventId;
    private static int announcementId;
    private static int questionId;
    public static boolean value = true;


    private static final String DATABASE_NAME = "settings_database";

    private static final String REGEX_EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";
    private static final String REGEX_NAME_PATTERN = "^[\\p{L} .'-]+$";
    private static final String REGEX_MOBILE_NO_PATTERN = "^[6-9]\\d{9}$";
    private static final String REGEX_DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";


    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference UserReference;

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }


    public Utils(Context context) {
        this.context = context;

    }

    public static String getRegexMobileNoPattern() {
        return REGEX_MOBILE_NO_PATTERN;
    }

    public static String getRegexDatePattern() {
        return REGEX_DATE_PATTERN;
    }

    public static String getRegexEmailPattern() {
        return REGEX_EMAIL_PATTERN;
    }

    public static String getRegexNamePattern() {
        return REGEX_NAME_PATTERN;
    }


    public static int getEventId() {
        return eventId;
    }


    public static void setLastEnrollmentNo() {
        Log.d(TAG, "setLastEnrollmentNo: called");
        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("lastEnrollmentNo");
        myRef.setValue("20200000").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: lastEnrollment no is set");
                } else {
                    Log.d(TAG, "onComplete: error occurred during setting last enrollment no " + task.getException().getLocalizedMessage());
                }
            }
        });

    }

    public static String getTerms() {
        String value = "\n" +
                "Terms and Conditions\n" +
                "\n" +
                "Welcome to www.labs-technology.com!\n" +
                "\n" +
                "These terms and conditions outline the rules and regulations for the use of labs-technology's Website, located at https://www.linkedin.com/company/labs-technology/.\n" +
                "\n" +
                "By accessing this website we assume you accept these terms and conditions. Do not continue to use www.labs-technology.com if you do not agree to take all of the terms and conditions stated on this page. Our Terms and Conditions were created with the help of the Terms And Conditions Generator and the Free Terms & Conditions Generator.\n" +
                "\n" +
                "The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and all Agreements: \"Client\", \"You\" and \"Your\" refers to you, the person log on this website and compliant to the Company’s terms and conditions. \"The Company\", \"Ourselves\", \"We\", \"Our\" and \"Us\", refers to our Company. \"Party\", \"Parties\", or \"Us\", refers to both the Client and ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner for the express purpose of meeting the Client’s needs in respect of provision of the Company’s stated services, in accordance with and subject to, prevailing law of Netherlands. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same.\n" +
                "Cookies\n" +
                "\n" +
                "We employ the use of cookies. By accessing www.labs-technology.com, you agreed to use cookies in agreement with the labs-technology's Privacy Policy.\n" +
                "\n" +
                "Most interactive websites use cookies to let us retrieve the user’s details for each visit. Cookies are used by our website to enable the functionality of certain areas to make it easier for people visiting our website. Some of our affiliate/advertising partners may also use cookies.\n" +
                "License\n" +
                "\n" +
                "Unless otherwise stated, labs-technology and/or its licensors own the intellectual property rights for all material on www.labs-technology.com. All intellectual property rights are reserved. You may access this from www.labs-technology.com for your own personal use subjected to restrictions set in these terms and conditions.\n" +
                "\n" +
                "You must not:\n" +
                "\n" +
                "    Republish material from www.labs-technology.com\n" +
                "    Sell, rent or sub-license material from www.labs-technology.com\n" +
                "    Reproduce, duplicate or copy material from www.labs-technology.com\n" +
                "    Redistribute content from www.labs-technology.com\n" +
                "\n" +
                "This Agreement shall begin on the date hereof.\n" +
                "\n" +
                "Parts of this website offer an opportunity for users to post and exchange opinions and information in certain areas of the website. labs-technology does not filter, edit, publish or review Comments prior to their presence on the website. Comments do not reflect the views and opinions of labs-technology,its agents and/or affiliates. Comments reflect the views and opinions of the person who post their views and opinions. To the extent permitted by applicable laws, labs-technology shall not be liable for the Comments or for any liability, damages or expenses caused and/or suffered as a result of any use of and/or posting of and/or appearance of the Comments on this website.\n" +
                "\n" +
                "labs-technology reserves the right to monitor all Comments and to remove any Comments which can be considered inappropriate, offensive or causes breach of these Terms and Conditions.\n" +
                "\n" +
                "You warrant and represent that:\n" +
                "\n" +
                "    You are entitled to post the Comments on our website and have all necessary licenses and consents to do so;\n" +
                "    The Comments do not invade any intellectual property right, including without limitation copyright, patent or trademark of any third party;\n" +
                "    The Comments do not contain any defamatory, libelous, offensive, indecent or otherwise unlawful material which is an invasion of privacy\n" +
                "    The Comments will not be used to solicit or promote business or custom or present commercial activities or unlawful activity.\n" +
                "\n" +
                "You hereby grant labs-technology a non-exclusive license to use, reproduce, edit and authorize others to use, reproduce and edit any of your Comments in any and all forms, formats or media.\n" +
                "Hyperlinking to our Content\n" +
                "\n" +
                "The following organizations may link to our Website without prior written approval:\n" +
                "\n" +
                "    Government agencies;\n" +
                "    Search engines;\n" +
                "    News organizations;\n" +
                "    Online directory distributors may link to our Website in the same manner as they hyperlink to the Websites of other listed businesses; and\n" +
                "    System wide Accredited Businesses except soliciting non-profit organizations, charity shopping malls, and charity fundraising groups which may not hyperlink to our Web site.\n" +
                "\n" +
                "These organizations may link to our home page, to publications or to other Website information so long as the link: (a) is not in any way deceptive; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and its products and/or services; and (c) fits within the context of the linking party’s site.\n" +
                "\n" +
                "We may consider and approve other link requests from the following types of organizations:\n" +
                "\n" +
                "    commonly-known consumer and/or business information sources;\n" +
                "    dot.com community sites;\n" +
                "    associations or other groups representing charities;\n" +
                "    online directory distributors;\n" +
                "    internet portals;\n" +
                "    accounting, law and consulting firms; and\n" +
                "    educational institutions and trade associations.\n" +
                "\n" +
                "We will approve link requests from these organizations if we decide that: (a) the link would not make us look unfavorably to ourselves or to our accredited businesses; (b) the organization does not have any negative records with us; (c) the benefit to us from the visibility of the hyperlink compensates the absence of labs-technology; and (d) the link is in the context of general resource information.\n" +
                "\n" +
                "These organizations may link to our home page so long as the link: (a) is not in any way deceptive; (b) does not falsely imply sponsorship, endorsement or approval of the linking party and its products or services; and (c) fits within the context of the linking party’s site.\n" +
                "\n" +
                "If you are one of the organizations listed in paragraph 2 above and are interested in linking to our website, you must inform us by sending an e-mail to labs-technology. Please include your name, your organization name, contact information as well as the URL of your site, a list of any URLs from which you intend to link to our Website, and a list of the URLs on our site to which you would like to link. Wait 2-3 weeks for a response.\n" +
                "\n" +
                "Approved organizations may hyperlink to our Website as follows:\n" +
                "\n" +
                "    By use of our corporate name; or\n" +
                "    By use of the uniform resource locator being linked to; or\n" +
                "    By use of any other description of our Website being linked to that makes sense within the context and format of content on the linking party’s site.\n" +
                "\n" +
                "No use of labs-technology's logo or other artwork will be allowed for linking absent a trademark license agreement.\n" +
                "iFrames\n" +
                "\n" +
                "Without prior approval and written permission, you may not create frames around our Webpages that alter in any way the visual presentation or appearance of our Website.\n" +
                "Content Liability\n" +
                "\n" +
                "We shall not be hold responsible for any content that appears on your Website. You agree to protect and defend us against all claims that is rising on your Website. No link(s) should appear on any Website that may be interpreted as libelous, obscene or criminal, or which infringes, otherwise violates, or advocates the infringement or other violation of, any third party rights.\n" +
                "Your Privacy\n" +
                "\n" +
                "Please read Privacy Policy\n" +
                "Reservation of Rights\n" +
                "\n" +
                "We reserve the right to request that you remove all links or any particular link to our Website. You approve to immediately remove all links to our Website upon request. We also reserve the right to amen these terms and conditions and it’s linking policy at any time. By continuously linking to our Website, you agree to be bound to and follow these linking terms and conditions.\n" +
                "Removal of links from our website\n" +
                "\n" +
                "If you find any link on our Website that is offensive for any reason, you are free to contact and inform us any moment. We will consider requests to remove links but we are not obligated to or so or to respond to you directly.\n" +
                "\n" +
                "We do not ensure that the information on this website is correct, we do not warrant its completeness or accuracy; nor do we promise to ensure that the website remains available or that the material on the website is kept up to date.\n" +
                "Disclaimer\n" +
                "\n" +
                "To the maximum extent permitted by applicable law, we exclude all representations, warranties and conditions relating to our website and the use of this website. Nothing in this disclaimer will:\n" +
                "\n" +
                "    limit or exclude our or your liability for death or personal injury;\n" +
                "    limit or exclude our or your liability for fraud or fraudulent misrepresentation;\n" +
                "    limit any of our or your liabilities in any way that is not permitted under applicable law; or\n" +
                "    exclude any of our or your liabilities that may not be excluded under applicable law.\n" +
                "\n" +
                "The limitations and prohibitions of liability set in this Section and elsewhere in this disclaimer: (a) are subject to the preceding paragraph; and (b) govern all liabilities arising under the disclaimer, including liabilities arising in contract, in tort and for breach of statutory duty.\n" +
                "\n" +
                "As long as the website and the information and services on the website are provided free of charge, we will not be liable for any loss or damage of any nature.\n";
        return value;
    }

    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        Log.d(TAG, "isImageFile: file Type" + mimeType);
        return mimeType != null && mimeType.indexOf("image") == 0;

    }

    public static String getCurrentUid() {
        Log.d(TAG, "getCurrentUid: called");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        return currentUserID;
    }

    /**
     * @param time        in milliseconds (Timestamp)
     * @param mDateFormat SimpleDateFormat
     * @return
     */
    public static String getDateTimeFromTimeStamp(Long time, String mDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateTime = new Date(time);
        return dateFormat.format(dateTime);
    }

    public static void findLastEnrollmentNo(final FireBaseCallBack callBack) {
        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("lastEnrollmentNo");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: datasnapshot" + dataSnapshot);
                String lastEnrollmentNo = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: got last enrollment no" + lastEnrollmentNo);
                callBack.onSuccess(lastEnrollmentNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void updateLastEnrollmentNo(String enrollmentNo) {
        Log.d(TAG, "updateLastEnrollmentNo: called");
        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("lastEnrollmentNo");
        myRef.setValue(enrollmentNo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: updating of enrollment no is complete");
                } else {
                    Log.d(TAG, "onComplete: error occurred during the updating of enrollment no" + task.getException().getLocalizedMessage());
                }
            }
        });


    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public void setSignedIn(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("signedIn", value);
        editor.apply();

    }

    public static String formatDateToFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, new Locale("en"));
        //dateFormat.setTimeZone(TimeZone.getTimeZone("Local"));
        return dateFormat.format(date);
    }

    public static String convertMillsToFormat(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String getTPartyNotices() {
        String value = "android-gif-drawable 5.0.0 | MIT License\n" +
                "Copyright (c) 2013 - present Karol Wrótniak, Droids on Roids LLC\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in\n" +
                "all copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
                "THE SOFTWARE.\n" +
                "\n" +
                "SKIA:\n" +
                "// Copyright (c) 2011 Google Inc. All rights reserved.\n" +
                "//\n" +
                "// Redistribution and use in source and binary forms, with or without\n" +
                "// modification, are permitted provided that the following conditions are\n" +
                "// met:\n" +
                "//\n" +
                "//    * Redistributions of source code must retain the above copyright\n" +
                "// notice, this list of conditions and the following disclaimer.\n" +
                "//    * Redistributions in binary form must reproduce the above\n" +
                "// copyright notice, this list of conditions and the following disclaimer\n" +
                "// in the documentation and/or other materials provided with the\n" +
                "// distribution.\n" +
                "//    * Neither the name of Google Inc. nor the names of its\n" +
                "// contributors may be used to endorse or promote products derived from\n" +
                "// this software without specific prior written permission.\n" +
                "//\n" +
                "// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n" +
                "// \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT\n" +
                "// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR\n" +
                "// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT\n" +
                "// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,\n" +
                "// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT\n" +
                "// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n" +
                "// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n" +
                "// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
                "// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n" +
                "// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "\n" +
                "GIFLIB:\n" +
                "The GIFLIB distribution is Copyright (c) 1997  Eric S. Raymond\n" +
                "\n" +
                "ReLinker:\n" +
                "Copyright 2015 KeepSafe Software, Inc.\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n" +
                "\n" +
                "memset.arm.S:\n" +
                "Copyright (C) 2010 The Android Open Source Project\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "     http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n" +
                "\t\t\t\t\t\n" +
                "Dagger 2.19 | Apache License\n" +
                "Copyright 2012 The Dagger Authors\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n" +
                "                    \n" +
                "com.android.support:appcompat-v7 27.1.1 | Apache 2.0\n" +
                "Copyright (C) 2017 The Android Open Source Project\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";
        return value;
    }

    public boolean isUserAttemptThisQuiz(Quiz quiz) {
        Log.d(TAG, "isUserAttemptAssignment: called");
        ArrayList<User> usersAttempted = quiz.getAttemptedBy();
        if (usersAttempted == null) {
            return false;
        }
        for (User user : usersAttempted) {
            if (user.getuId().equals(Utils.getCurrentUid())) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserAttemptingThisQuiz(Quiz quiz) {
        Log.d(TAG, "isUserAttemptingThisQuiz: called");
        ArrayList<User> usersAttempting = quiz.getAttempting();
        if (usersAttempting == null) {
            return false;
        }
        for (User user : usersAttempting) {
            if (user.getuId().equals(Utils.getCurrentUid())) {
                return true;
            }
        }

        return false;
    }

    public QuizScore getQuizScore(Quiz quiz) {
        Log.d(TAG, "getQuizScore: called");

        ArrayList<QuizScore> scores = quiz.getScores();

        for (QuizScore score : scores
        ) {
            if (score.getuId().equals(getCurrentUid())) {
                return score;
            }
        }
        return new QuizScore();
    }

    public void setAckAnnouncement(Announcement ackAnnouncement) {
        Log.d(TAG, "setAckAnnouncement: called" + ackAnnouncement.getId());
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        String value = sharedPreferences.getString("ackAnnouncements", null);
        ArrayList<String> oldAcks = gson.fromJson(value, type);

        if (oldAcks != null) {
            oldAcks.add(ackAnnouncement.getId());
            editor.putString("ackAnnouncements", gson.toJson(oldAcks));
            editor.commit();
        } else {
            oldAcks = new ArrayList<>();
            oldAcks.add(ackAnnouncement.getId());
            editor.putString("ackAnnouncements", gson.toJson(oldAcks));
            editor.commit();
        }

    }

    public ArrayList<String> getAckAnnouncements() {
        Log.d(TAG, "getAckAnnouncements: called");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("ackAnnouncements", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> ackAnnouncements = gson.fromJson(value, type);
        if (ackAnnouncements != null) {
            Log.d(TAG, "getAckAnnouncements: ackAnnouncements " + ackAnnouncements.toString());
            return ackAnnouncements;
        }
        return new ArrayList<>();

    }

    public boolean isNewAnnouncement(Announcement announcement) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> ackAnnouncements = getAckAnnouncements();
        if (announcement != null) {
            if (ackAnnouncements.contains(announcement.getId())) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void setDarkThemePreference(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkThemePreference", value);
        editor.apply();
    }

    public static boolean getDarkThemePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("darkThemePreference", false);
    }

    public boolean isAdmin() {
        Log.d(TAG, "isAdmin: called");
        //TODO: write proper logic
        return true;
    }

    public static void setTheme(Context context) {
        if (Utils.getDarkThemePreference(context)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void checkUserExistence(final FireBaseCallBack fireBaseCallBack) {
        Log.d(TAG, "checkUserExistence: called");
        mAuth = FirebaseAuth.getInstance();
        UserReference = FirebaseDatabaseReference.DATABASE.getReference().child("User");
        final String current_user_id = mAuth.getCurrentUser().getUid();


        UserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(current_user_id)) {
                    fireBaseCallBack.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fireBaseCallBack.onError(databaseError);
            }
        });


    }

    public static String getPrivacyPolicy() {
        String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Turpis in eu mi bibendum neque. Massa tincidunt dui ut ornare. Urna molestie at elementum eu facilisis. Facilisi nullam vehicula ipsum a arcu cursus vitae congue mauris. Purus in mollis nunc sed id semper risus in hendrerit. Tempor id eu nisl nunc mi ipsum faucibus vitae aliquet. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor purus. Nisi est sit amet facilisis magna etiam tempor orci eu. Mauris ultrices eros in cursus turpis massa tincidunt dui. Arcu odio ut sem nulla pharetra diam sit. Habitant morbi tristique senectus et netus et malesuada fames ac. Sed augue lacus viverra vitae congue eu consequat. Sagittis purus sit amet volutpat consequat mauris nunc congue. In nibh mauris cursus mattis molestie. Erat imperdiet sed euismod nisi porta. Vivamus at augue eget arcu.\n" +
                "\n" +
                "Praesent tristique magna sit amet purus gravida quis blandit. Nisi vitae suscipit tellus mauris a. Amet justo donec enim diam vulputate ut. Mattis pellentesque id nibh tortor id. Aliquam purus sit amet luctus venenatis. Urna molestie at elementum eu. Diam sollicitudin tempor id eu nisl nunc. Id aliquet risus feugiat in ante metus dictum at. Pulvinar elementum integer enim neque volutpat ac tincidunt vitae semper. Praesent elementum facilisis leo vel fringilla est. Erat imperdiet sed euismod nisi porta lorem mollis aliquam ut. Fusce ut placerat orci nulla. Lacus vel facilisis volutpat est. Diam in arcu cursus euismod quis viverra nibh cras pulvinar. Tortor vitae purus faucibus ornare suspendisse sed nisi lacus sed. Placerat duis ultricies lacus sed turpis tincidunt id. Sed nisi lacus sed viverra tellus in. Integer quis auctor elit sed vulputate mi sit.\n" +
                "\n" +
                "Facilisi morbi tempus iaculis urna id volutpat lacus laoreet non. A iaculis at erat pellentesque adipiscing commodo elit at. Donec pretium vulputate sapien nec sagittis. At ultrices mi tempus imperdiet nulla malesuada pellentesque. Gravida in fermentum et sollicitudin ac orci phasellus egestas. Dui sapien eget mi proin sed libero. Ornare arcu dui vivamus arcu felis bibendum ut tristique et. Varius morbi enim nunc faucibus a pellentesque. Eget velit aliquet sagittis id consectetur purus ut faucibus pulvinar. Ac odio tempor orci dapibus ultrices. Felis eget velit aliquet sagittis id consectetur purus ut faucibus. Nulla aliquet enim tortor at auctor.\n" +
                "\n" +
                "Non enim praesent elementum facilisis leo vel fringilla. Leo in vitae turpis massa sed elementum tempus egestas. Ornare aenean euismod elementum nisi quis eleifend quam adipiscing. Felis imperdiet proin fermentum leo vel orci. Adipiscing elit pellentesque habitant morbi tristique senectus et netus. Diam ut venenatis tellus in metus vulputate eu scelerisque. Vitae ultricies leo integer malesuada nunc vel. Vitae turpis massa sed elementum tempus egestas sed sed risus. Vehicula ipsum a arcu cursus vitae congue mauris rhoncus aenean. Sed cras ornare arcu dui vivamus. Arcu odio ut sem nulla pharetra diam sit amet. Pulvinar elementum integer enim neque volutpat ac tincidunt. Iaculis nunc sed augue lacus viverra vitae congue.\n" +
                "\n" +
                "Quis vel eros donec ac odio tempor orci dapibus. Sem viverra aliquet eget sit amet tellus cras. Vitae justo eget magna fermentum iaculis eu non diam. Mattis rhoncus urna neque viverra. Viverra vitae congue eu consequat ac. Ultrices neque ornare aenean euismod elementum nisi quis eleifend. Sed faucibus turpis in eu mi. Non sodales neque sodales ut etiam sit. Aliquam etiam erat velit scelerisque in dictum non. Dignissim sodales ut eu sem integer vitae. Convallis convallis tellus id interdum. In nulla posuere sollicitudin aliquam ultrices sagittis orci a scelerisque. Suspendisse faucibus interdum posuere lorem. Ac turpis egestas sed tempus urna et pharetra. Nec feugiat nisl pretium fusce id velit ut tortor pretium. Nunc mattis enim ut tellus elementum sagittis vitae et. Mi ipsum faucibus vitae aliquet nec ullamcorper sit amet. Turpis nunc eget lorem dolor sed viverra.\n" +
                "\n" +
                "Arcu cursus vitae congue mauris rhoncus aenean vel elit scelerisque. Scelerisque felis imperdiet proin fermentum leo vel. Etiam tempor orci eu lobortis elementum nibh tellus. Mattis ullamcorper velit sed ullamcorper morbi tincidunt ornare massa eget. Lorem donec massa sapien faucibus et molestie ac feugiat. Elit ut aliquam purus sit amet luctus venenatis. Vulputate dignissim suspendisse in est ante in nibh. Mollis nunc sed id semper risus in hendrerit. Tincidunt id aliquet risus feugiat. Vitae semper quis lectus nulla at volutpat diam ut venenatis. Sed sed risus pretium quam vulputate dignissim suspendisse in est. Eros in cursus turpis massa tincidunt dui ut ornare. Risus viverra adipiscing at in tellus integer feugiat. Est velit egestas dui id ornare arcu. Ullamcorper morbi tincidunt ornare massa eget egestas purus. Eu volutpat odio facilisis mauris sit amet massa. Odio morbi quis commodo odio aenean sed.\n" +
                "\n" +
                "Eu ultrices vitae auctor eu augue ut lectus arcu. Sit amet consectetur adipiscing elit ut aliquam purus sit. Nunc id cursus metus aliquam eleifend mi. At consectetur lorem donec massa. Gravida quis blandit turpis cursus in hac habitasse platea. Nisl condimentum id venenatis a. Faucibus in ornare quam viverra orci sagittis. Integer quis auctor elit sed. Justo nec ultrices dui sapien. Aliquet bibendum enim facilisis gravida.\n" +
                "\n" +
                "Est lorem ipsum dolor sit amet. Tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum. Pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu vitae. Ac turpis egestas sed tempus urna et. Quis enim lobortis scelerisque fermentum. Ut consequat semper viverra nam libero. Et ultrices neque ornare aenean euismod elementum nisi. Facilisi morbi tempus iaculis urna id. Amet commodo nulla facilisi nullam vehicula. Mattis molestie a iaculis at. Leo vel orci porta non pulvinar neque. Est pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus. Sem fringilla ut morbi tincidunt augue interdum velit. Sed elementum tempus egestas sed sed risus pretium. Tristique sollicitudin nibh sit amet commodo nulla facilisi nullam. Elit pellentesque habitant morbi tristique senectus et. Nulla facilisi etiam dignissim diam quis enim lobortis scelerisque.\n" +
                "\n" +
                "Aliquam ut porttitor leo a. Fringilla urna porttitor rhoncus dolor purus non. Sapien et ligula ullamcorper malesuada proin libero nunc consequat interdum. Nulla facilisi cras fermentum odio eu feugiat pretium. Neque ornare aenean euismod elementum nisi quis eleifend. Ac auctor augue mauris augue. Faucibus et molestie ac feugiat sed lectus. Sed vulputate mi sit amet mauris commodo quis imperdiet massa. At tempor commodo ullamcorper a. Turpis tincidunt id aliquet risus feugiat in ante. Hendrerit dolor magna eget est lorem ipsum. Mi tempus imperdiet nulla malesuada pellentesque elit. Proin sagittis nisl rhoncus mattis rhoncus urna neque viverra justo. Viverra suspendisse potenti nullam ac tortor vitae. Pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies. Quam elementum pulvinar etiam non quam lacus suspendisse. Scelerisque felis imperdiet proin fermentum leo vel orci. Vitae et leo duis ut.\n" +
                "\n" +
                "Scelerisque eu ultrices vitae auctor eu augue ut lectus arcu. Nunc sed blandit libero volutpat. Auctor eu augue ut lectus. Viverra nam libero justo laoreet sit amet. Varius duis at consectetur lorem donec massa. Adipiscing elit pellentesque habitant morbi tristique senectus et netus et. Aliquam etiam erat velit scelerisque in dictum non consectetur. Vel pharetra vel turpis nunc eget lorem. Vitae nunc sed velit dignissim sodales ut eu. Convallis a cras semper auctor neque vitae. Turpis in eu mi bibendum neque egestas congue. Accumsan in nisl nisi scelerisque eu ultrices. Nullam non nisi est sit amet. Gravida dictum fusce ut placerat orci nulla. Malesuada fames ac turpis egestas maecenas pharetra convallis. Viverra aliquet eget sit amet tellus cras adipiscing enim eu. Tempus quam pellentesque nec nam aliquam sem.\n" +
                "\n" +
                "Nisi porta lorem mollis aliquam ut porttitor. Vitae proin sagittis nisl rhoncus mattis. Tincidunt dui ut ornare lectus sit amet est placerat in. Aliquam sem fringilla ut morbi tincidunt augue interdum. Hac habitasse platea dictumst vestibulum rhoncus est. In egestas erat imperdiet sed euismod nisi. Vestibulum sed arcu non odio. Lobortis elementum nibh tellus molestie nunc non blandit massa enim. Nunc sed augue lacus viverra vitae congue eu. Praesent semper feugiat nibh sed pulvinar proin gravida hendrerit.\n" +
                "\n" +
                "Nibh cras pulvinar mattis nunc sed. Ipsum dolor sit amet consectetur adipiscing. Nulla aliquet porttitor lacus luctus accumsan tortor. Justo donec enim diam vulputate. Orci sagittis eu volutpat odio facilisis mauris sit amet. In hendrerit gravida rutrum quisque non tellus. Commodo sed egestas egestas fringilla phasellus faucibus scelerisque. Magnis dis parturient montes nascetur ridiculus mus mauris vitae. Amet risus nullam eget felis. Sed euismod nisi porta lorem mollis aliquam. Consectetur adipiscing elit ut aliquam purus sit. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor purus.\n" +
                "\n" +
                "Ornare suspendisse sed nisi lacus sed viverra tellus in hac. Et odio pellentesque diam volutpat commodo. Nisl vel pretium lectus quam id leo in. Libero id faucibus nisl tincidunt eget nullam non nisi. Aliquet risus feugiat in ante metus. Platea dictumst quisque sagittis purus sit. Vulputate enim nulla aliquet porttitor. Sit amet porttitor eget dolor morbi non arcu. Pellentesque nec nam aliquam sem. Sed id semper risus in hendrerit. Iaculis at erat pellentesque adipiscing commodo. Fermentum iaculis eu non diam phasellus vestibulum lorem sed risus. Turpis egestas pretium aenean pharetra magna ac placerat. Sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur. Tincidunt dui ut ornare lectus sit amet est placerat. Odio euismod lacinia at quis risus. Placerat in egestas erat imperdiet. Diam quis enim lobortis scelerisque. Placerat duis ultricies lacus sed turpis tincidunt.\n" +
                "\n" +
                "Ultrices dui sapien eget mi proin. Ac auctor augue mauris augue neque. Aliquam etiam erat velit scelerisque in dictum. Interdum varius sit amet mattis vulputate enim nulla aliquet porttitor. Tellus cras adipiscing enim eu turpis egestas pretium. Mauris pellentesque pulvinar pellentesque habitant morbi tristique senectus et. Neque volutpat ac tincidunt vitae semper. Laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget. Amet commodo nulla facilisi nullam vehicula ipsum a arcu. Aliquam purus sit amet luctus venenatis. Eu consequat ac felis donec et odio pellentesque diam. Egestas sed sed risus pretium quam vulputate dignissim. Aliquet nec ullamcorper sit amet risus nullam eget felis. Consectetur adipiscing elit pellentesque habitant morbi. Maecenas pharetra convallis posuere morbi. Quam vulputate dignissim suspendisse in est ante. Magna fringilla urna porttitor rhoncus dolor purus non enim praesent.\n" +
                "\n" +
                "Aenean sed adipiscing diam donec. Viverra accumsan in nisl nisi scelerisque eu ultrices. Cursus euismod quis viverra nibh cras pulvinar mattis nunc. Aliquam ut porttitor leo a diam sollicitudin tempor id. Duis tristique sollicitudin nibh sit amet. Mollis nunc sed id semper risus in hendrerit gravida. Faucibus purus in massa tempor nec. Augue eget arcu dictum varius. Blandit volutpat maecenas volutpat blandit aliquam. Tristique risus nec feugiat in fermentum posuere urna. Morbi blandit cursus risus at ultrices mi. Lobortis feugiat vivamus at augue eget. Dictum sit amet justo donec enim diam. Tincidunt praesent semper feugiat nibh sed pulvinar proin gravida hendrerit. In cursus turpis massa tincidunt dui ut ornare lectus. Eget nunc lobortis mattis aliquam faucibus purus.\n" +
                "\n" +
                "Sed augue lacus viverra vitae congue eu consequat ac. Nulla facilisi morbi tempus iaculis urna. Eget nullam non nisi est. Elementum curabitur vitae nunc sed velit. Adipiscing vitae proin sagittis nisl. Urna id volutpat lacus laoreet. Et magnis dis parturient montes. Scelerisque mauris pellentesque pulvinar pellentesque habitant morbi tristique senectus. Quam quisque id diam vel quam elementum pulvinar etiam non. Purus viverra accumsan in nisl nisi scelerisque eu ultrices vitae. Nunc vel risus commodo viverra maecenas. Imperdiet sed euismod nisi porta lorem mollis aliquam ut. Volutpat commodo sed egestas egestas.\n" +
                "\n" +
                "Eu facilisis sed odio morbi. Elementum curabitur vitae nunc sed velit. Sed cras ornare arcu dui vivamus arcu felis bibendum ut. Vel pretium lectus quam id leo in. Fusce id velit ut tortor pretium viverra. Bibendum at varius vel pharetra. Elit ut aliquam purus sit amet luctus. Vitae proin sagittis nisl rhoncus mattis rhoncus urna neque. Erat nam at lectus urna. Egestas quis ipsum suspendisse ultrices gravida dictum. Venenatis cras sed felis eget velit aliquet. Eu lobortis elementum nibh tellus molestie nunc non. Vel turpis nunc eget lorem dolor sed viverra. Sagittis aliquam malesuada bibendum arcu vitae. Id consectetur purus ut faucibus pulvinar. Nulla malesuada pellentesque elit eget gravida cum sociis natoque. Ullamcorper morbi tincidunt ornare massa eget egestas purus viverra accumsan.\n" +
                "\n" +
                "Pellentesque nec nam aliquam sem et tortor consequat id porta. Suspendisse in est ante in. Aliquet porttitor lacus luctus accumsan tortor posuere ac. Nulla malesuada pellentesque elit eget gravida cum sociis natoque penatibus. Urna nunc id cursus metus aliquam eleifend mi in. Quam id leo in vitae turpis. Amet nulla facilisi morbi tempus iaculis urna. Sed adipiscing diam donec adipiscing. Nisl purus in mollis nunc. Eu lobortis elementum nibh tellus molestie nunc non. Mauris cursus mattis molestie a iaculis at erat pellentesque adipiscing. Tristique nulla aliquet enim tortor at auctor urna nunc id. Risus in hendrerit gravida rutrum.\n" +
                "\n" +
                "Viverra nibh cras pulvinar mattis nunc sed blandit. Ultrices tincidunt arcu non sodales. Mi eget mauris pharetra et ultrices neque ornare aenean euismod. Facilisi etiam dignissim diam quis enim lobortis scelerisque fermentum dui. Congue nisi vitae suscipit tellus mauris a diam. Pellentesque id nibh tortor id aliquet lectus proin nibh nisl. Aliquam etiam erat velit scelerisque in dictum. Arcu vitae elementum curabitur vitae nunc. In fermentum et sollicitudin ac orci. Convallis tellus id interdum velit laoreet id donec. Risus in hendrerit gravida rutrum quisque non tellus. Dictum sit amet justo donec enim diam vulputate. Justo nec ultrices dui sapien eget mi proin. Neque laoreet suspendisse interdum consectetur libero id. Nec tincidunt praesent semper feugiat nibh sed. Neque aliquam vestibulum morbi blandit cursus risus at ultrices mi. In fermentum posuere urna nec tincidunt praesent. Volutpat odio facilisis mauris sit. Senectus et netus et malesuada fames ac turpis. Neque aliquam vestibulum morbi blandit.\n" +
                "\n" +
                "Diam maecenas sed enim ut sem. Sit amet porttitor eget dolor morbi non arcu risus. Purus sit amet volutpat consequat. In eu mi bibendum neque egestas congue quisque egestas diam. Mattis molestie a iaculis at erat pellentesque adipiscing commodo elit. Ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet. Id interdum velit laoreet id donec ultrices tincidunt. Adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Feugiat vivamus at augue eget arcu dictum. Quis hendrerit dolor magna eget est lorem ipsum dolor sit.\n" +
                "\n" +
                "Id semper risus in hendrerit gravida rutrum quisque non. Nec sagittis aliquam malesuada bibendum arcu vitae. Dui nunc mattis enim ut tellus elementum. Gravida neque convallis a cras semper auctor neque vitae tempus. Mi quis hendrerit dolor magna eget est lorem ipsum dolor. Quam vulputate dignissim suspendisse in est ante in. Sagittis purus sit amet volutpat consequat mauris. Odio eu feugiat pretium nibh ipsum consequat. Consequat nisl vel pretium lectus quam id leo. Risus sed vulputate odio ut enim blandit. Iaculis urna id volutpat lacus. Ultrices in iaculis nunc sed augue lacus. Eget mauris pharetra et ultrices neque ornare. Dignissim convallis aenean et tortor at risus viverra.\n" +
                "\n" +
                "Velit dignissim sodales ut eu. Ultricies integer quis auctor elit sed. Facilisis sed odio morbi quis commodo odio. Dictumst vestibulum rhoncus est pellentesque elit ullamcorper dignissim. Amet cursus sit amet dictum sit amet justo donec enim. Tempor nec feugiat nisl pretium fusce id velit. Purus sit amet volutpat consequat mauris nunc. Feugiat pretium nibh ipsum consequat nisl vel. Arcu cursus vitae congue mauris rhoncus aenean. Turpis egestas integer eget aliquet nibh praesent tristique magna. Ipsum consequat nisl vel pretium lectus quam id leo. Congue eu consequat ac felis. Ut sem viverra aliquet eget sit. Facilisis leo vel fringilla est ullamcorper eget nulla facilisi. Nisl nisi scelerisque eu ultrices. Nunc faucibus a pellentesque sit amet porttitor. Sed viverra ipsum nunc aliquet bibendum enim facilisis gravida neque. Turpis egestas pretium aenean pharetra magna.\n" +
                "\n" +
                "Semper viverra nam libero justo. Mauris in aliquam sem fringilla. Suscipit tellus mauris a diam maecenas sed enim. Ipsum suspendisse ultrices gravida dictum fusce. Et malesuada fames ac turpis egestas sed. Felis eget velit aliquet sagittis id consectetur purus. A erat nam at lectus urna duis convallis convallis tellus. Et tortor consequat id porta nibh venenatis cras. Natoque penatibus et magnis dis parturient montes nascetur ridiculus mus. Scelerisque varius morbi enim nunc faucibus. Leo integer malesuada nunc vel risus commodo viverra maecenas accumsan. Erat velit scelerisque in dictum non consectetur a. Donec et odio pellentesque diam volutpat commodo sed egestas egestas. Ut enim blandit volutpat maecenas volutpat blandit aliquam etiam erat. Fermentum posuere urna nec tincidunt praesent semper feugiat nibh sed. Suspendisse sed nisi lacus sed viverra tellus in hac habitasse. Morbi quis commodo odio aenean sed. Sagittis vitae et leo duis ut diam quam.\n" +
                "\n" +
                "Amet commodo nulla facilisi nullam vehicula ipsum a arcu cursus. Adipiscing elit ut aliquam purus sit amet luctus. Malesuada bibendum arcu vitae elementum curabitur vitae nunc sed. Non blandit massa enim nec dui nunc mattis enim. Risus sed vulputate odio ut enim blandit volutpat. Eget dolor morbi non arcu. Augue mauris augue neque gravida. Tellus id interdum velit laoreet id donec ultrices. Tellus cras adipiscing enim eu turpis egestas pretium. Vestibulum mattis ullamcorper velit sed ullamcorper morbi. Amet est placerat in egestas erat imperdiet sed euismod. Ac ut consequat semper viverra nam. Aliquam ut porttitor leo a. Ac odio tempor orci dapibus ultrices in iaculis nunc sed. In hac habitasse platea dictumst. Rhoncus aenean vel elit scelerisque mauris pellentesque.\n" +
                "\n" +
                "Pellentesque eu tincidunt tortor aliquam nulla. Velit laoreet id donec ultrices tincidunt arcu. Condimentum id venenatis a condimentum vitae. Vulputate eu scelerisque felis imperdiet proin fermentum. Lacus vestibulum sed arcu non odio euismod lacinia at quis. Odio euismod lacinia at quis risus sed vulputate. Sit amet cursus sit amet dictum sit amet justo. Vel turpis nunc eget lorem dolor sed viverra ipsum. Varius morbi enim nunc faucibus a pellentesque. Sem et tortor consequat id porta nibh. Lacus vel facilisis volutpat est velit egestas dui id. Vivamus arcu felis bibendum ut tristique. Sit amet dictum sit amet justo. Eu consequat ac felis donec et odio pellentesque diam. Enim neque volutpat ac tincidunt vitae. In fermentum et sollicitudin ac orci phasellus egestas tellus rutrum. Tellus orci ac auctor augue. Scelerisque fermentum dui faucibus in ornare quam viverra orci. A condimentum vitae sapien pellentesque habitant morbi. Consequat id porta nibh venenatis.\n" +
                "\n" +
                "Tellus elementum sagittis vitae et leo duis ut diam quam. Faucibus et molestie ac feugiat sed lectus vestibulum mattis. Tortor consequat id porta nibh venenatis cras. Mattis molestie a iaculis at erat pellentesque adipiscing. Montes nascetur ridiculus mus mauris vitae ultricies leo. Nibh venenatis cras sed felis eget velit aliquet sagittis. Elementum sagittis vitae et leo duis ut. Imperdiet massa tincidunt nunc pulvinar sapien et. Morbi tristique senectus et netus et. Ut faucibus pulvinar elementum integer enim neque volutpat. Enim sed faucibus turpis in eu mi bibendum neque. Risus at ultrices mi tempus. Nunc mattis enim ut tellus elementum sagittis vitae et leo. Risus nullam eget felis eget. Orci a scelerisque purus semper eget duis at.\n" +
                "\n" +
                "Nullam non nisi est sit amet facilisis magna. Sit amet tellus cras adipiscing enim eu turpis egestas pretium. Iaculis nunc sed augue lacus. Sodales neque sodales ut etiam sit amet nisl purus. Turpis massa sed elementum tempus egestas sed sed. Amet consectetur adipiscing elit ut. Porta non pulvinar neque laoreet suspendisse interdum consectetur libero. Fames ac turpis egestas integer eget. Commodo viverra maecenas accumsan lacus vel facilisis volutpat. Ac tortor dignissim convallis aenean et tortor. Lorem ipsum dolor sit amet. Montes nascetur ridiculus mus mauris vitae ultricies leo integer malesuada. Suspendisse in est ante in nibh mauris cursus. Volutpat sed cras ornare arcu. Ac felis donec et odio pellentesque diam volutpat. Ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet. Dignissim sodales ut eu sem integer vitae justo eget. Consectetur a erat nam at.\n" +
                "\n" +
                "Tincidunt eget nullam non nisi. Id aliquet risus feugiat in. Amet consectetur adipiscing elit ut aliquam. Pellentesque elit eget gravida cum sociis. Tellus rutrum tellus pellentesque eu tincidunt tortor. In arcu cursus euismod quis viverra nibh cras pulvinar. Morbi non arcu risus quis varius quam quisque. Pretium quam vulputate dignissim suspendisse in est ante in nibh. Lobortis feugiat vivamus at augue eget. Natoque penatibus et magnis dis parturient montes. Nulla facilisi cras fermentum odio eu. Quis auctor elit sed vulputate mi. Quisque sagittis purus sit amet volutpat consequat mauris nunc congue. Arcu ac tortor dignissim convallis aenean et tortor at. Imperdiet dui accumsan sit amet nulla facilisi morbi tempus. Feugiat pretium nibh ipsum consequat nisl vel pretium lectus.\n" +
                "\n" +
                "Massa sed elementum tempus egestas. Nunc consequat interdum varius sit. Dui vivamus arcu felis bibendum ut tristique et egestas. Congue eu consequat ac felis donec. Dui id ornare arcu odio ut sem. Vitae congue eu consequat ac felis donec et odio. Tempor orci eu lobortis elementum nibh tellus molestie nunc non. Vitae justo eget magna fermentum iaculis eu non. Ultricies mi eget mauris pharetra et. Velit dignissim sodales ut eu sem.\n" +
                "\n" +
                "Mauris rhoncus aenean vel elit scelerisque mauris. Senectus et netus et malesuada fames ac turpis egestas sed. Dictum sit amet justo donec enim diam vulputate ut. Nunc mattis enim ut tellus. Justo nec ultrices dui sapien eget. Nulla posuere sollicitudin aliquam ultrices sagittis orci a scelerisque. Ipsum suspendisse ultrices gravida dictum fusce ut placerat orci nulla. Nisl suscipit adipiscing bibendum est ultricies. Dignissim convallis aenean et tortor at risus viverra adipiscing at. Facilisis volutpat est velit egestas dui id ornare arcu. Pellentesque habitant morbi tristique senectus et netus et. Elementum pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Eget nunc scelerisque viverra mauris in aliquam. Morbi blandit cursus risus at ultrices mi tempus imperdiet. Viverra aliquet eget sit amet tellus. In ornare quam viverra orci sagittis eu volutpat odio facilisis. Lacus laoreet non curabitur gravida.";
        return value;
    }

    public void getCurrentUser(final FireBaseCallBack fireBaseCallBack) {
        Log.d(TAG, "getCurrentUser: called");


        UserReference = FirebaseDatabaseReference.DATABASE.getReference()
                .child(FirebaseConstants.USERS).child(getCurrentUid());
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setuId(Utils.getCurrentUid());
                    Log.d(TAG, "onDataChange: user" + user.toString());
                    fireBaseCallBack.onSuccess(user);
                } else {
                    fireBaseCallBack.onSuccess(new User());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fireBaseCallBack.onError(databaseError);
            }
        });


    }

    public boolean isUserAttemptThisAssignment(Assignment assignment) {
        Log.d(TAG, "isUserAttemptAssignment: called");
        ArrayList<User> usersAttempted = new ArrayList<>();
        if (assignment != null) {
            usersAttempted = assignment.getAttemptedBy();
        }
        if (usersAttempted == null) {
            return false;
        }
        for (User user : usersAttempted) {
            if (user.getuId().equals(Utils.getCurrentUid())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> readDocxFile(File file) {
        String text = "";
        ArrayList<String> lines = new ArrayList<>();
        try {

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);


            List<XWPFParagraph> paragraphs = document.getParagraphs();


            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
                lines.addAll(Arrays.asList(para.getText().split("\n")));

            }

            XWPFWordExtractor we = new XWPFWordExtractor(document);
            System.out.println(we.getText());
            text = we.getText();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }


    public ArrayList<MultipleChoiceQuestion> getQuestionsFromFile(File file) {
        ArrayList<String> lines = readDocxFile(file);
        ArrayList<MultipleChoiceQuestion> questions = new ArrayList<>();
        ArrayList<String> qs = new ArrayList<>();

//        for (int i = 0; i < lines.size(); i++) {
//            String s = lines.get(i);
//            question.setQuestion(s);
//            for ( j = i; j < j + 4; j++) {
//                if(j<lines.size()){
//                    options.add(lines.get(j));
//                }else{
//                    options.add("");
//                }
//
//
//            }
//            question.setOptions(options);
//            questions.add(question);
//            i = j + 1;

//        }


        Log.d(TAG, "getQuestionsFromFile: lines " + lines.toString());
        Log.d(TAG, "getQuestionsFromFile: lines size" + lines.size());
        ArrayList<String> options = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (i % 6 == 0) {
                qs.add(lines.get(i));
            } else {
                options.add(lines.get(i));
            }

        }
        int k = 0;
        for (String q : qs
        ) {
            MultipleChoiceQuestion question = new MultipleChoiceQuestion();
            question.setQuestion(q);
            ArrayList<String> tempOptions = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                if (i < options.size() && i != 4 && k != options.size()) {
                    tempOptions.add(options.get(k));
                    k++;
                }
                if (i == 4 && k != options.size()) {
                    question.setAnswer(options.get(k));
                    k++;
                }


            }
            question.setOptions(tempOptions);
            question.setId(String.valueOf(System.currentTimeMillis()));
            questions.add(question);
        }
        Log.d(TAG, "getQuestionsFromFile: questions " + questions.toString());
        return questions;

    }


}
