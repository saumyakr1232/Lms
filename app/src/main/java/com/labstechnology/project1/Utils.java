package com.labstechnology.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.database.FirebaseDatabase;
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
        Log.d(TAG, "getTerms: called");
        String terms = "\n" +
                "<h2 style=\"text-align: center;\"><b>TERMS AND CONDITIONS</b></h2>\n" +
                "<p>Last updated: 2020-06-16</p>\n" +
                "<p>1. <b>Introduction</b></p>\n" +
                "<p>Welcome to <b>labstechnologies</b> (“Company”, “we”, “our”, “us”)!</p>\n" +
                "<p>These Terms of Service (“Terms”, “Terms of Service”) govern your use of our website located at <b>labstechnologies.in</b> (together or individually “Service”) operated by <b>labstechnologies</b>.</p>\n" +
                "<p>Our Privacy Policy also governs your use of our Service and explains how we collect, safeguard and disclose information that results from your use of our web pages.</p>\n" +
                "<p>Your agreement with us includes these Terms and our Privacy Policy (“Agreements”). You acknowledge that you have read and understood Agreements, and agree to be bound of them.</p>\n" +
                "<p>If you do not agree with (or cannot comply with) Agreements, then you may not use the Service, but please let us know by emailing at <b>connect@labstechnologies.in</b> so we can try to find a solution. These Terms apply to all visitors, users and others who wish to access or use Service.</p>\n" +
                "<p>2. <b>Communications</b></p>\n" +
                "<p>By using our Service, you agree to subscribe to newsletters, marketing or promotional materials and other information we may send. However, you may opt out of receiving any, or all, of these communications from us by following the unsubscribe link or by emailing at connect@labstechnologies.in.</p>\n" +
                "\n" +
                "<p>3. <b>Contests, Sweepstakes and Promotions</b></p>\n" +
                "<p>Any contests, sweepstakes or other promotions (collectively, “Promotions”) made available through Service may be governed by rules that are separate from these Terms of Service. If you participate in any Promotions, please review the applicable rules as well as our Privacy Policy. If the rules for a Promotion conflict with these Terms of Service, Promotion rules will apply.</p>\n" +
                "\n" +
                "\n" +
                "<p>4. <b>Content</b></p><p>Our Service allows you to post, link, store, share and otherwise make available certain information, text, graphics, videos, or other material (“Content”). You are responsible for Content that you post on or through Service, including its legality, reliability, and appropriateness.</p><p>By posting Content on or through Service, You represent and warrant that: (i) Content is yours (you own it) and/or you have the right to use it and the right to grant us the rights and license as provided in these Terms, and (ii) that the posting of your Content on or through Service does not violate the privacy rights, publicity rights, copyrights, contract rights or any other rights of any person or entity. We reserve the right to terminate the account of anyone found to be infringing on a copyright.</p><p>You retain any and all of your rights to any Content you submit, post or display on or through Service and you are responsible for protecting those rights. We take no responsibility and assume no liability for Content you or any third party posts on or through Service. However, by posting Content using Service you grant us the right and license to use, modify, publicly perform, publicly display, reproduce, and distribute such Content on and through Service. You agree that this license includes the right for us to make your Content available to other users of Service, who may also use your Content subject to these Terms.</p><p>labstechnologies has the right but not the obligation to monitor and edit all Content provided by users.</p><p>In addition, Content found on or through this Service are the property of labstechnologies or used with permission. You may not distribute, modify, transmit, reuse, download, repost, copy, or use said Content, whether in whole or in part, for commercial purposes or for personal gain, without express advance written permission from us.</p>\n" +
                "<p>5. <b>Prohibited Uses</b></p>\n" +
                "<p>You may use Service only for lawful purposes and in accordance with Terms. You agree not to use Service:</p>\n" +
                "<p>0.1. In any way that violates any applicable national or international law or regulation.</p>\n" +
                "<p>0.2. For the purpose of exploiting, harming, or attempting to exploit or harm minors in any way by exposing them to inappropriate content or otherwise.</p>\n" +
                "<p>0.3. To transmit, or procure the sending of, any advertising or promotional material, including any “junk mail”, “chain letter,” “spam,” or any other similar solicitation.</p>\n" +
                "<p>0.4. To impersonate or attempt to impersonate Company, a Company employee, another user, or any other person or entity.</p>\n" +
                "<p>0.5. In any way that infringes upon the rights of others, or in any way is illegal, threatening, fraudulent, or harmful, or in connection with any unlawful, illegal, fraudulent, or harmful purpose or activity.</p>\n" +
                "<p>0.6. To engage in any other conduct that restricts or inhibits anyone’s use or enjoyment of Service, or which, as determined by us, may harm or offend Company or users of Service or expose them to liability.</p>\n" +
                "<p>Additionally, you agree not to:</p>\n" +
                "<p>0.1. Use Service in any manner that could disable, overburden, damage, or impair Service or interfere with any other party’s use of Service, including their ability to engage in real time activities through Service.</p>\n" +
                "<p>0.2. Use any robot, spider, or other automatic device, process, or means to access Service for any purpose, including monitoring or copying any of the material on Service.</p>\n" +
                "<p>0.3. Use any manual process to monitor or copy any of the material on Service or for any other unauthorized purpose without our prior written consent.</p>\n" +
                "<p>0.4. Use any device, software, or routine that interferes with the proper working of Service.</p>\n" +
                "<p>0.5. Introduce any viruses, trojan horses, worms, logic bombs, or other material which is malicious or technologically harmful.</p>\n" +
                "<p>0.6. Attempt to gain unauthorized access to, interfere with, damage, or disrupt any parts of Service, the server on which Service is stored, or any server, computer, or database connected to Service.</p>\n" +
                "<p>0.7. Attack Service via a denial-of-service attack or a distributed denial-of-service attack.</p>\n" +
                "<p>0.8. Take any action that may damage or falsify Company rating.</p>\n" +
                "<p>0.9. Otherwise attempt to interfere with the proper working of Service.</p>\n" +
                "<p>6. <b>Analytics</b></p>\n" +
                "<p>We may use third-party Service Providers to monitor and analyze the use of our Service.</p>\n" +
                "<p>7. <b>No Use By Minors</b></p>\n" +
                "<p>Service is intended only for access and use by individuals at least eighteen (18) years old. By accessing or using Service, you warrant and represent that you are at least eighteen (18) years of age and with the full authority, right, and capacity to enter into this agreement and abide by all of the terms and conditions of Terms. If you are not at least eighteen (18) years old, you are prohibited from both the access and usage of Service.</p>\n" +
                "<p>8. <b>Accounts</b></p><p>When you create an account with us, you guarantee that you are above the age of 18, and that the information you provide us is accurate, complete, and current at all times. Inaccurate, incomplete, or obsolete information may result in the immediate termination of your account on Service.</p><p>You are responsible for maintaining the confidentiality of your account and password, including but not limited to the restriction of access to your computer and/or account. You agree to accept responsibility for any and all activities or actions that occur under your account and/or password, whether your password is with our Service or a third-party service. You must notify us immediately upon becoming aware of any breach of security or unauthorized use of your account.</p><p>You may not use as a username the name of another person or entity or that is not lawfully available for use, a name or trademark that is subject to any rights of another person or entity other than you, without appropriate authorization. You may not use as a username any name that is offensive, vulgar or obscene.</p><p>We reserve the right to refuse service, terminate accounts, remove or edit content, or cancel orders in our sole discretion.</p>\n" +
                "<p>9. <b>Intellectual Property</b></p>\n" +
                "<p>Service and its original content (excluding Content provided by users), features and functionality are and will remain the exclusive property of labstechnologies and its licensors. Service is protected by copyright, trademark, and other laws of  and foreign countries. Our trademarks may not be used in connection with any product or service without the prior written consent of labstechnologies.</p>\n" +
                "<p>10. <b>Copyright Policy</b></p>\n" +
                "<p>We respect the intellectual property rights of others. It is our policy to respond to any claim that Content posted on Service infringes on the copyright or other intellectual property rights (“Infringement”) of any person or entity.</p>\n" +
                "<p>If you are a copyright owner, or authorized on behalf of one, and you believe that the copyrighted work has been copied in a way that constitutes copyright infringement, please submit your claim via email to connect@labstechnologies.in, with the subject line: “Copyright Infringement” and include in your claim a detailed description of the alleged Infringement as detailed below, under “DMCA Notice and Procedure for Copyright Infringement Claims”</p>\n" +
                "<p>You may be held accountable for damages (including costs and attorneys’ fees) for misrepresentation or bad-faith claims on the infringement of any Content found on and/or through Service on your copyright.</p>\n" +
                "<p>11. <b>DMCA Notice and Procedure for Copyright Infringement Claims</b></p>\n" +
                "<p>You may submit a notification pursuant to the Digital Millennium Copyright Act (DMCA) by providing our Copyright Agent with the following information in writing (see 17 U.S.C 512(c)(3) for further detail):</p>\n" +
                "<p>0.1. an electronic or physical signature of the person authorized to act on behalf of the owner of the copyright’s interest;</p>\n" +
                "<p>0.2. a description of the copyrighted work that you claim has been infringed, including the URL (i.e., web page address) of the location where the copyrighted work exists or a copy of the copyrighted work;</p>\n" +
                "<p>0.3. identification of the URL or other specific location on Service where the material that you claim is infringing is located;</p>\n" +
                "<p>0.4. your address, telephone number, and email address;</p>\n" +
                "<p>0.5. a statement by you that you have a good faith belief that the disputed use is not authorized by the copyright owner, its agent, or the law;</p>\n" +
                "<p>0.6. a statement by you, made under penalty of perjury, that the above information in your notice is accurate and that you are the copyright owner or authorized to act on the copyright owner’s behalf.</p>\n" +
                "<p>You can contact our Copyright Agent via email at connect@labstechnologies.in.</p>\n" +
                "<p>12. <b>Error Reporting and Feedback</b></p>\n" +
                "<p>You may provide us either directly at connect@labstechnologies.in or via third party sites and tools with information and feedback concerning errors, suggestions for improvements, ideas, problems, complaints, and other matters related to our Service (“Feedback”). You acknowledge and agree that: (i) you shall not retain, acquire or assert any intellectual property right or other right, title or interest in or to the Feedback; (ii) Company may have development ideas similar to the Feedback; (iii) Feedback does not contain confidential information or proprietary information from you or any third party; and (iv) Company is not under any obligation of confidentiality with respect to the Feedback. In the event the transfer of the ownership to the Feedback is not possible due to applicable mandatory laws, you grant Company and its affiliates an exclusive, transferable, irrevocable, free-of-charge, sub-licensable, unlimited and perpetual right to use (including copy, modify, create derivative works, publish, distribute and commercialize) Feedback in any manner and for any purpose.</p>\n" +
                "<p>13. <b>Links To Other Web Sites</b></p>\n" +
                "<p>Our Service may contain links to third party web sites or services that are not owned or controlled by labstechnologies.</p>\n" +
                "<p>labstechnologies has no control over, and assumes no responsibility for the content, privacy policies, or practices of any third party web sites or services. We do not warrant the offerings of any of these entities/individuals or their websites.</p>\n" +
                "<p>For example, the outlined <a href=\"https://policymaker.io/terms-and-conditions/\">Terms of Service</a> have been created using <a href=\"https://policymaker.io/\">PolicyMaker.io</a>, a free web application for generating high-quality legal documents. PolicyMaker’s <a href=\"https://policymaker.io/terms-and-conditions/\">free Terms and Conditions generator</a> is an easy-to-use free tool for creating an excellent standard Terms of Service template for a website, blog, e-commerce store or app.</p>\n" +
                "<p>YOU ACKNOWLEDGE AND AGREE THAT COMPANY SHALL NOT BE RESPONSIBLE OR LIABLE, DIRECTLY OR INDIRECTLY, FOR ANY DAMAGE OR LOSS CAUSED OR ALLEGED TO BE CAUSED BY OR IN CONNECTION WITH USE OF OR RELIANCE ON ANY SUCH CONTENT, GOODS OR SERVICES AVAILABLE ON OR THROUGH ANY SUCH THIRD PARTY WEB SITES OR SERVICES.</p>\n" +
                "<p>WE STRONGLY ADVISE YOU TO READ THE TERMS OF SERVICE AND PRIVACY POLICIES OF ANY THIRD PARTY WEB SITES OR SERVICES THAT YOU VISIT.</p>\n" +
                "<p>14. <b>Disclaimer Of Warranty</b></p>\n" +
                "<p>THESE SERVICES ARE PROVIDED BY COMPANY ON AN “AS IS” AND “AS AVAILABLE” BASIS. COMPANY MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND, EXPRESS OR IMPLIED, AS TO THE OPERATION OF THEIR SERVICES, OR THE INFORMATION, CONTENT OR MATERIALS INCLUDED THEREIN. YOU EXPRESSLY AGREE THAT YOUR USE OF THESE SERVICES, THEIR CONTENT, AND ANY SERVICES OR ITEMS OBTAINED FROM US IS AT YOUR SOLE RISK.</p>\n" +
                "<p>NEITHER COMPANY NOR ANY PERSON ASSOCIATED WITH COMPANY MAKES ANY WARRANTY OR REPRESENTATION WITH RESPECT TO THE COMPLETENESS, SECURITY, RELIABILITY, QUALITY, ACCURACY, OR AVAILABILITY OF THE SERVICES. WITHOUT LIMITING THE FOREGOING, NEITHER COMPANY NOR ANYONE ASSOCIATED WITH COMPANY REPRESENTS OR WARRANTS THAT THE SERVICES, THEIR CONTENT, OR ANY SERVICES OR ITEMS OBTAINED THROUGH THE SERVICES WILL BE ACCURATE, RELIABLE, ERROR-FREE, OR UNINTERRUPTED, THAT DEFECTS WILL BE CORRECTED, THAT THE SERVICES OR THE SERVER THAT MAKES IT AVAILABLE ARE FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS OR THAT THE SERVICES OR ANY SERVICES OR ITEMS OBTAINED THROUGH THE SERVICES WILL OTHERWISE MEET YOUR NEEDS OR EXPECTATIONS.</p>\n" +
                "<p>COMPANY HEREBY DISCLAIMS ALL WARRANTIES OF ANY KIND, WHETHER EXPRESS OR IMPLIED, STATUTORY, OR OTHERWISE, INCLUDING BUT NOT LIMITED TO ANY WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT, AND FITNESS FOR PARTICULAR PURPOSE.</p>\n" +
                "<p>THE FOREGOING DOES NOT AFFECT ANY WARRANTIES WHICH CANNOT BE EXCLUDED OR LIMITED UNDER APPLICABLE LAW.</p>\n" +
                "<p>15. <b>Limitation Of Liability</b></p>\n" +
                "<p>EXCEPT AS PROHIBITED BY LAW, YOU WILL HOLD US AND OUR OFFICERS, DIRECTORS, EMPLOYEES, AND AGENTS HARMLESS FOR ANY INDIRECT, PUNITIVE, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGE, HOWEVER IT ARISES (INCLUDING ATTORNEYS’ FEES AND ALL RELATED COSTS AND EXPENSES OF LITIGATION AND ARBITRATION, OR AT TRIAL OR ON APPEAL, IF ANY, WHETHER OR NOT LITIGATION OR ARBITRATION IS INSTITUTED), WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE, OR OTHER TORTIOUS ACTION, OR ARISING OUT OF OR IN CONNECTION WITH THIS AGREEMENT, INCLUDING WITHOUT LIMITATION ANY CLAIM FOR PERSONAL INJURY OR PROPERTY DAMAGE, ARISING FROM THIS AGREEMENT AND ANY VIOLATION BY YOU OF ANY FEDERAL, STATE, OR LOCAL LAWS, STATUTES, RULES, OR REGULATIONS, EVEN IF COMPANY HAS BEEN PREVIOUSLY ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. EXCEPT AS PROHIBITED BY LAW, IF THERE IS LIABILITY FOUND ON THE PART OF COMPANY, IT WILL BE LIMITED TO THE AMOUNT PAID FOR THE PRODUCTS AND/OR SERVICES, AND UNDER NO CIRCUMSTANCES WILL THERE BE CONSEQUENTIAL OR PUNITIVE DAMAGES. SOME STATES DO NOT ALLOW THE EXCLUSION OR LIMITATION OF PUNITIVE, INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE PRIOR LIMITATION OR EXCLUSION MAY NOT APPLY TO YOU.</p>\n" +
                "<p>16. <b>Termination</b></p>\n" +
                "<p>We may terminate or suspend your account and bar access to Service immediately, without prior notice or liability, under our sole discretion, for any reason whatsoever and without limitation, including but not limited to a breach of Terms.</p>\n" +
                "<p>If you wish to terminate your account, you may simply discontinue using Service.</p>\n" +
                "<p>All provisions of Terms which by their nature should survive termination shall survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity and limitations of liability.</p>\n" +
                "<p>17. <b>Governing Law</b></p>\n" +
                "<p>These Terms shall be governed and construed in accordance with the laws of India, which governing law applies to agreement without regard to its conflict of law provisions.</p>\n" +
                "<p>Our failure to enforce any right or provision of these Terms will not be considered a waiver of those rights. If any provision of these Terms is held to be invalid or unenforceable by a court, the remaining provisions of these Terms will remain in effect. These Terms constitute the entire agreement between us regarding our Service and supersede and replace any prior agreements we might have had between us regarding Service.</p>\n" +
                "<p>18. <b>Changes To Service</b></p>\n" +
                "<p>We reserve the right to withdraw or amend our Service, and any service or material we provide via Service, in our sole discretion without notice. We will not be liable if for any reason all or any part of Service is unavailable at any time or for any period. From time to time, we may restrict access to some parts of Service, or the entire Service, to users, including registered users.</p>\n" +
                "<p>19. <b>Amendments To Terms</b></p>\n" +
                "<p>We may amend Terms at any time by posting the amended terms on this site. It is your responsibility to review these Terms periodically.</p>\n" +
                "<p>Your continued use of the Platform following the posting of revised Terms means that you accept and agree to the changes. You are expected to check this page frequently so you are aware of any changes, as they are binding on you.</p>\n" +
                "<p>By continuing to access or use our Service after any revisions become effective, you agree to be bound by the revised terms. If you do not agree to the new terms, you are no longer authorized to use Service.</p>\n" +
                "<p>20. <b>Waiver And Severability</b></p>\n" +
                "<p>No waiver by Company of any term or condition set forth in Terms shall be deemed a further or continuing waiver of such term or condition or a waiver of any other term or condition, and any failure of Company to assert a right or provision under Terms shall not constitute a waiver of such right or provision.</p>\n" +
                "<p>If any provision of Terms is held by a court or other tribunal of competent jurisdiction to be invalid, illegal or unenforceable for any reason, such provision shall be eliminated or limited to the minimum extent such that the remaining provisions of Terms will continue in full force and effect.</p>\n" +
                "<p>21. <b>Acknowledgement</b></p>\n" +
                "<p>BY USING SERVICE OR OTHER SERVICES PROVIDED BY US, YOU ACKNOWLEDGE THAT YOU HAVE READ THESE TERMS OF SERVICE AND AGREE TO BE BOUND BY THEM.</p>\n" +
                "<p>22. <b>Contact Us</b></p>\n" +
                "<p>Please send your feedback, comments, requests for technical support by email: <b>connect@labstechnologies.in</b>.</p>\n" +
                "<p style=\"margin-top: 5em; font-size: 0.7em;\">These <a href=\"https://policymaker.io/terms-conditions/\">Terms and Conditions</a> were created for <b>labstechnologies.in</b> by <a href=\"https://policymaker.io\">PolicyMaker.io</a> on 2020-06-16.</p>\n";

        return terms;

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

    public void checkForAdmin(final FireBaseCallBack fireBaseCallBack) {
        Log.d(TAG, "checkForAdmin: called");
        FirebaseDatabase database = FirebaseDatabaseReference.DATABASE;
        DatabaseReference myRef = database.getReference("admin");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: snapshot" + dataSnapshot.toString());
                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: TEST" + oneSnapshot);
                    Log.d(TAG, "onDataChange: TEST" + oneSnapshot.getKey());
                    Log.d(TAG, "onDataChange: TEST" + Utils.getCurrentUid());
                    if (oneSnapshot.getKey().equals(Utils.getCurrentUid())) {
                        Log.d(TAG, "onDataChange: TEST User Is admin");
                        fireBaseCallBack.onSuccess(Boolean.TRUE);
                    } else {
                        fireBaseCallBack.onSuccess(Boolean.FALSE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fireBaseCallBack.onSuccess(1);
            }
        });
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
        Log.d(TAG, "getPrivacyPolicy: called");
        String string = "\n" +
                "<h2 style=\"text-align: center;\"><b>PRIVACY POLICY</b></h2>\n" +
                "<p>Effective date: 2020-06-17</p>\n" +
                "<p>1. <b>Introduction</b></p>\n" +
                "<p>Welcome to <b> Labstechnologies</b>.</p>\n" +
                "<p><b>Labstechnologies</b> (“us”, “we”, or “our”) operates <b>labstechnologies.in</b> (hereinafter referred to as <b>“Service”</b>).</p>\n" +
                "<p>Our Privacy Policy governs your visit to <b>labstechnologies.in</b>, and explains how we collect, safeguard and disclose information that results from your use of our Service.</p>\n" +
                "<p>We use your data to provide and improve Service. By using Service, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, the terms used in this Privacy Policy have the same meanings as in our Terms and Conditions.</p>\n" +
                "<p>Our Terms and Conditions (<b>“Terms”</b>) govern all use of our Service and together with the Privacy Policy constitutes your agreement with us (<b>“agreement”</b>).</p>\n" +
                "<p>2. <b>Definitions</b></p>\n" +
                "<p><b>SERVICE</b> means the labstechnologies.in website operated by Labstechnologies.</p>\n" +
                "<p><b>PERSONAL DATA</b> means data about a living individual who can be identified from those data (or from those and other information either in our possession or likely to come into our possession).</p>\n" +
                "<p><b>USAGE DATA</b> is data collected automatically either generated by the use of Service or from Service infrastructure itself (for example, the duration of a page visit).</p>\n" +
                "<p><b>COOKIES</b> are small files stored on your device (computer or mobile device).</p>\n" +
                "<p><b>DATA CONTROLLER</b> means a natural or legal person who (either alone or jointly or in common with other persons) determines the purposes for which and the manner in which any personal data are, or are to be, processed. For the purpose of this Privacy Policy, we are a Data Controller of your data.</p>\n" +
                "<p><b>DATA PROCESSORS (OR SERVICE PROVIDERS)</b> means any natural or legal person who processes the data on behalf of the Data Controller. We may use the services of various Service Providers in order to process your data more effectively.</p> <p><b>DATA SUBJECT</b> is any living individual who is the subject of Personal Data.</p>\n" +
                "<p><b>THE USER</b> is the individual using our Service. The User corresponds to the Data Subject, who is the subject of Personal Data.</p>\n" +
                "<p>3. <b>Information Collection and Use</b></p>\n" +
                "<p>We collect several different types of information for various purposes to provide and improve our Service to you.</p>\n" +
                "<p>4. <b>Types of Data Collected</b></p>\n" +
                "<p><b>Personal Data</b></p>\n" +
                "<p>While using our Service, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you (<b>“Personal Data”</b>). Personally identifiable information may include, but is not limited to:</p>\n" +
                "<p>0.1. Email address</p>\n" +
                "<p>0.2. First name and last name</p>\n" +
                "<p>0.3. Phone number</p>\n" +
                "<p>0.4. Address, Country, State, Province, ZIP/Postal code, City</p>\n" +
                "<p>0.5. Cookies and Usage Data</p>\n" +
                "<p>We may use your Personal Data to contact you with newsletters, marketing or promotional materials and other information that may be of interest to you. You may opt out of receiving any, or all, of these communications from us by following the unsubscribe link.</p>\n" +
                "<p><b>Usage Data</b></p>\n" +
                "<p>We may also collect information that your browser sends whenever you visit our Service or when you access Service by or through any device (<b>“Usage Data”</b>).</p>\n" +
                "<p>This Usage Data may include information such as your computer’s Internet Protocol address (e.g. IP address), browser type, browser version, the pages of our Service that you visit, the time and date of your visit, the time spent on those pages, unique device identifiers and other diagnostic data.</p>\n" +
                "<p>When you access Service with a device, this Usage Data may include information such as the type of device you use, your device unique ID, the IP address of your device, your device operating system, the type of Internet browser you use, unique device identifiers and other diagnostic data.</p>\n" +
                "\n" +
                "<p><b>Tracking Cookies Data</b></p>\n" +
                "<p>We use cookies and similar tracking technologies to track the activity on our Service and we hold certain information.</p>\n" +
                "<p>Cookies are files with a small amount of data which may include an anonymous unique identifier. Cookies are sent to your browser from a website and stored on your device. Other tracking technologies are also used such as beacons, tags and scripts to collect and track information and to improve and analyze our Service.</p>\n" +
                "<p>You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Service.</p>\n" +
                "<p>Examples of Cookies we use:</p>\n" +
                "<p>0.1. <b>Session Cookies:</b> We use Session Cookies to operate our Service.</p>\n" +
                "<p>0.2. <b>Preference Cookies:</b> We use Preference Cookies to remember your preferences and various settings.</p>\n" +
                "<p>0.3. <b>Security Cookies:</b> We use Security Cookies for security purposes.</p>\n" +
                "<p>0.4. <b>Advertising Cookies:</b> Advertising Cookies are used to serve you with advertisements that may be relevant to you and your interests.</p>\n" +
                "<p><b>Other Data</b></p>\n" +
                "<p>While using our Service, we may also collect the following information: sex, age, date of birth, place of birth, passport details, citizenship, registration at place of residence and actual address, telephone number (work, mobile), details of documents on education, qualification, professional training, employment agreements, <a href=\"https://policymaker.io/non-disclosure-agreement/\">non-disclosure agreements</a>, information on bonuses and compensation, information on marital status, family members, social security (or other taxpayer identification) number, office location and other data.</p>\n" +
                "<p>5. <b>Use of Data</b></p>\n" +
                "<p>Labstechnologies uses the collected data for various purposes:</p>\n" +
                "<p>0.1. to provide and maintain our Service;</p>\n" +
                "<p>0.2. to notify you about changes to our Service;</p>\n" +
                "<p>0.3. to allow you to participate in interactive features of our Service when you choose to do so;</p>\n" +
                "<p>0.4. to provide customer support;</p>\n" +
                "<p>0.5. to gather analysis or valuable information so that we can improve our Service;</p>\n" +
                "<p>0.6. to monitor the usage of our Service;</p>\n" +
                "<p>0.7. to detect, prevent and address technical issues;</p>\n" +
                "<p>0.8. to fulfil any other purpose for which you provide it;</p>\n" +
                "<p>0.9. to carry out our obligations and enforce our rights arising from any contracts entered into between you and us, including for billing and collection;</p>\n" +
                "<p>0.10. to provide you with notices about your account and/or subscription, including expiration and renewal notices, email-instructions, etc.;</p>\n" +
                "<p>0.11. to provide you with news, special offers and general information about other goods, services and events which we offer that are similar to those that you have already purchased or enquired about unless you have opted not to receive such information;</p>\n" +
                "<p>0.12. in any other way we may describe when you provide the information;</p>\n" +
                "<p>0.13. for any other purpose with your consent.</p>\n" +
                "<p>6. <b>Retention of Data</b></p>\n" +
                "<p>We will retain your Personal Data only for as long as is necessary for the purposes set out in this Privacy Policy. We will retain and use your Personal Data to the extent necessary to comply with our legal obligations (for example, if we are required to retain your data to comply with applicable laws), resolve disputes, and enforce our legal agreements and policies.</p>\n" +
                "<p>We will also retain Usage Data for internal analysis purposes. Usage Data is generally retained for a shorter period, except when this data is used to strengthen the security or to improve the functionality of our Service, or we are legally obligated to retain this data for longer time periods.</p>\n" +
                "<p>7. <b>Transfer of Data</b></p>\n" +
                "<p>Your information, including Personal Data, may be transferred to – and maintained on – computers located outside of your state, province, country or other governmental jurisdiction where the data protection laws may differ from those of your jurisdiction.</p>\n" +
                "<p>If you are located outside India and choose to provide information to us, please note that we transfer the data, including Personal Data, to India and process it there.</p>\n" +
                "<p>Your consent to this Privacy Policy followed by your submission of such information represents your agreement to that transfer.</p>\n" +
                "<p>Labstechnologies will take all the steps reasonably necessary to ensure that your data is treated securely and in accordance with this Privacy Policy and no transfer of your Personal Data will take place to an organisation or a country unless there are adequate controls in place including the security of your data and other personal information.</p>\n" +
                "<p>8. <b>Disclosure of Data</b></p>\n" +
                "<p>We may disclose personal information that we collect, or you provide:</p>\n" +
                "<p>0.1. <b>Disclosure for Law Enforcement.</b></p><p>Under certain circumstances, we may be required to disclose your Personal Data if required to do so by law or in response to valid requests by public authorities.</p><p>0.2. <b>Business Transaction.</b></p><p>If we or our subsidiaries are involved in a merger, acquisition or asset sale, your Personal Data may be transferred.</p><p>0.3. <b>Other cases. We may disclose your information also:</b></p><p>0.3.1. to our subsidiaries and affiliates;</p><p>0.3.2. to contractors, service providers, and other third parties we use to support our business;</p><p>0.3.3. to fulfill the purpose for which you provide it;</p><p>0.3.4. for the purpose of including your company’s logo on our website;</p><p>0.3.5. for any other purpose disclosed by us when you provide the information;</p><p>0.3.6. with your consent in any other cases;</p><p>0.3.7. if we believe disclosure is necessary or appropriate to protect the rights, property, or safety of the Company, our customers, or others.</p>\n" +
                "<p>9. <b>Security of Data</b></p>\n" +
                "<p>The security of your data is important to us but remember that no method of transmission over the Internet or method of electronic storage is 100% secure. While we strive to use commercially acceptable means to protect your Personal Data, we cannot guarantee its absolute security.</p>\n" +
                "<p>10. <b>Your Data Protection Rights Under General Data Protection Regulation (GDPR)\n" +
                "</b></p>\n" +
                "<p>If you are a resident of the European Union (EU) and European Economic Area (EEA), you have certain data protection rights, covered by GDPR.</p>\n" +
                "<p>We aim to take reasonable steps to allow you to correct, amend, delete, or limit the use of your Personal Data.</p>\n" +
                "<p> If you wish to be informed what Personal Data we hold about you and if you want it to be removed from our systems, please email us at <b>connect@labstechnologies.in</b>.</p>\n" +
                "<p>In certain circumstances, you have the following data protection rights:</p>\n" +
                "<p>0.1. the right to access, update or to delete the information we have on you;</p>\n" +
                "<p>0.2. the right of rectification. You have the right to have your information rectified if that information is inaccurate or incomplete;</p>\n" +
                "<p>0.3. the right to object. You have the right to object to our processing of your Personal Data;</p>\n" +
                "<p>0.4. the right of restriction. You have the right to request that we restrict the processing of your personal information;</p>\n" +
                "<p>0.5. the right to data portability. You have the right to be provided with a copy of your Personal Data in a structured, machine-readable and commonly used format;</p>\n" +
                "<p>0.6. the right to withdraw consent. You also have the right to withdraw your consent at any time where we rely on your consent to process your personal information;</p>\n" +
                "<p>Please note that we may ask you to verify your identity before responding to such requests. Please note, we may not able to provide Service without some necessary data.</p>\n" +
                "<p>You have the right to complain to a Data Protection Authority about our collection and use of your Personal Data. For more information, please contact your local data protection authority in the European Economic Area (EEA).</p>\n" +
                "<p>11. <b>Your Data Protection Rights under the California Privacy Protection Act (CalOPPA)</b></p>\n" +
                "<p>CalOPPA is the first state law in the nation to require commercial websites and online services to post a privacy policy. The law’s reach stretches well beyond California to require a person or company in the United States (and conceivable the world) that operates websites collecting personally identifiable information from California consumers to post a conspicuous privacy policy on its website stating exactly the information being collected and those individuals with whom it is being shared, and to comply with this policy.</p>\n" +
                "<p>According to CalOPPA we agree to the following:</p>\n" +
                "<p>0.1. users can visit our site anonymously;</p>\n" +
                "<p>0.2. our Privacy Policy link includes the word “Privacy”, and can easily be found on the home page of our website;</p>\n" +
                "<p>0.3. users will be notified of any privacy policy changes on our Privacy Policy Page;</p>\n" +
                "<p>0.4. users are able to change their personal information by emailing us at <b>connect@labstechnologies.in</b>.</p>\n" +
                "<p>Our Policy on “Do Not Track” Signals:</p>\n" +
                "<p>We honor Do Not Track signals and do not track, plant cookies, or use advertising when a Do Not Track browser mechanism is in place. Do Not Track is a preference you can set in your web browser to inform websites that you do not want to be tracked.</p>\n" +
                "<p>You can enable or disable Do Not Track by visiting the Preferences or Settings page of your web browser.</p>\n" +
                "<p>12. <b>Your Data Protection Rights under the California Consumer Privacy Act (CCPA)</b></p>\n" +
                "<p>If you are a California resident, you are entitled to learn what data we collect about you, ask to delete your data and not to sell (share) it. To exercise your data protection rights, you can make certain requests and ask us:</p>\n" +
                "<p><b>0.1. What personal information we have about you. If you make this request, we will return to you:</b></p>\n" +
                "<p>0.0.1. The categories of personal information we have collected about you.</p>\n" +
                "<p>0.0.2. The categories of sources from which we collect your personal information.</p>\n" +
                "<p>0.0.3. The business or commercial purpose for collecting or selling your personal information.</p>\n" +
                "<p>0.0.4. The categories of third parties with whom we share personal information.</p>\n" +
                "<p>0.0.5. The specific pieces of personal information we have collected about you.</p>\n" +
                "<p>0.0.6. A list of categories of personal information that we have sold, along with the category of any other company we sold it to. If we have not sold your personal information, we will inform you of that fact.</p>\n" +
                "<p>0.0.7. A list of categories of personal information that we have disclosed for a business purpose, along with the category of any other company we shared it with.</p>\n" +
                "<p>Please note, you are entitled to ask us to provide you with this information up to two times in a rolling twelve-month period. When you make this request, the information provided may be limited to the personal information we collected about you in the previous 12 months.</p>\n" +
                "<p><b>0.2. To delete your personal information. If you make this request, we will delete the personal information we hold about you as of the date of your request from our records and direct any service providers to do the same. In some cases, deletion may be accomplished through de-identification of the information. If you choose to delete your personal information, you may not be able to use certain functions that require your personal information to operate.</b></p>\n" +
                "<p><b>0.3. To stop selling your personal information. We don’t sell or rent your personal information to any third parties for any purpose. We do not sell your personal information for monetary consideration. However, under some circumstances, a transfer of personal information to a third party, or within our family of companies, without monetary consideration may be considered a “sale” under California law. You are the only owner of your Personal Data and can request disclosure or deletion at any time.</b></p>\n" +
                "<p>If you submit a request to stop selling your personal information, we will stop making such transfers.</p>\n" +
                "<p>Please note, if you ask us to delete or stop selling your data, it may impact your experience with us, and you may not be able to participate in certain programs or membership services which require the usage of your personal information to function. But in no circumstances, we will discriminate against you for exercising your rights.</p>\n" +
                "<p>To exercise your California data protection rights described above, please send your request(s) by email: <b>connect@labstechnologies.in</b>.</p>\n" +
                "<p>Your data protection rights, described above, are covered by the CCPA, short for the California Consumer Privacy Act. To find out more, visit the official California Legislative Information website. The CCPA took effect on 01/01/2020.</p>\n" +
                "<p>13. <b>Service Providers</b></p>\n" +
                "<p>We may employ third party companies and individuals to facilitate our Service (<b>“Service Providers”</b>), provide Service on our behalf, perform Service-related services or assist us in analysing how our Service is used.</p>\n" +
                "<p>These third parties have access to your Personal Data only to perform these tasks on our behalf and are obligated not to disclose or use it for any other purpose.</p>\n" +
                "<p>14. <b>Analytics</b></p>\n" +
                "<p>We may use third-party Service Providers to monitor and analyze the use of our Service.</p>\n" +
                "<p>15. <b>CI/CD tools</b></p>\n" +
                "<p>We may use third-party Service Providers to automate the development process of our Service.</p>\n" +
                "\n" +
                "<p>16. <b>Behavioral Remarketing</b></p>\n" +
                "<p>We may use remarketing services to advertise on third party websites to you after you visited our Service. We and our third-party vendors use cookies to inform, optimise and serve ads based on your past visits to our Service.</p>\n" +
                "\n" +
                "<p>17. <b>Links to Other Sites</b></p>\n" +
                "<p>Our Service may contain links to other sites that are not operated by us. If you click a third party link, you will be directed to that third party’s site. We strongly advise you to review the Privacy Policy of every site you visit.</p>\n" +
                "<p>We have no control over and assume no responsibility for the content, privacy policies or practices of any third party sites or services.</p>\n" +
                "<p>For example, the outlined Privacy Policy has been made using <a href=\"https://policymaker.io/\">PolicyMaker.io</a>, free tool that helps create high-quality legal documents. PolicyMaker’s free online <a href=\"https://policymaker.io/privacy-policy/\">privacy policy generator</a> is an easy-to-use tool for creating a <a href=\"https://policymaker.io/blog-privacy-policy/\">privacy policy for blog</a>, website, e-commerce store or mobile app.</p>\n" +
                "<p>18. <b><b>Children’s Privacy</b></b></p>\n" +
                "<p>Our Services are not intended for use by children under the age of 18 (<b>“Child”</b> or <b>“Children”</b>).</p>\n" +
                "<p>We do not knowingly collect personally identifiable information from Children under 18. If you become aware that a Child has provided us with Personal Data, please contact us. If we become aware that we have collected Personal Data from Children without verification of parental consent, we take steps to remove that information from our servers.</p>\n" +
                "<p>19. <b>Changes to This Privacy Policy</b></p>\n" +
                "<p>We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page.</p>\n" +
                "<p>We will let you know via email and/or a prominent notice on our Service, prior to the change becoming effective and update “effective date” at the top of this Privacy Policy.</p>\n" +
                "<p>You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.</p>\n" +
                "<p>20. <b>Contact Us</b></p>\n" +
                "<p>If you have any questions about this Privacy Policy, please contact us by email: <b>connect@labstechnologies.in</b>.</p>\n" +
                "<p style=\"margin-top: 5em; font-size: 0.7em;\">This <a href=\"https://policymaker.io/privacy-policy/\">Privacy Policy</a> was created for <b>labstechnologies.in</b> by <a href=\"https://policymaker.io\">PolicyMaker.io</a> on 2020-06-17.</p>\n";

        return string;
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

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
