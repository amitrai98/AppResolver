package android.stakbrowser.amitrai.andoridlibraryproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitrai on 5/1/16.
 */
public class AppResolver {

    // Tag to log error messages
    public static final String TAG = AppResolver.class.getSimpleName();


    /**
     * Created by amitrai
     *
     * chceks weather the app is installed or not.
     * @param act
     * @param Package_Name
     * @return
     */
    public static Intent isAppInstalled(Activity act, List<String> Package_Name){
        try {
            Intent App_Check_Intent = new Intent();
            App_Check_Intent.setType("text/plain");
            final PackageManager packageManager = act.getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(App_Check_Intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (int i = 0; i < Package_Name.size(); i++)
            {
                for (ResolveInfo resolveInfo : list)
                {
                    String p = resolveInfo.activityInfo.packageName;
                    if (p != null && p.startsWith(Package_Name.get(i)))
                    {
                        App_Check_Intent.setPackage(p);
                        return App_Check_Intent;
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG, "" + e);
            return  null;
        }
        return null;
    }

    /**
     * Created by amitrai
     *
     * automatically finds which app to open for the url supllied.
     */
    public static void resolve(Activity context, String webSiteUrl){

        try {
            if(context !=null && webSiteUrl != null && webSiteUrl.trim().length()>0){
                if(webSiteUrl.contains("www.youtube.com"))
                    openYouTube(context, webSiteUrl);
                else if(webSiteUrl.contains("twitter.com"))
                    openTwitterApp(context, webSiteUrl);
                else if(webSiteUrl.contains("www.facebook.com/"))
                    openFacebookApp(context, webSiteUrl);
                else if(webSiteUrl.contains("blogspot.com/"))
                    openBlogerApp(context, webSiteUrl);
                else if(webSiteUrl.contains("www.linkedin.com/"))
                    openLinkedin(context, webSiteUrl);
                else if(webSiteUrl.contains("plus.google.com/"))
                    openGPlus(context, webSiteUrl);
                else if(webSiteUrl.contains("www.pinterest.com/"))
                    openPinterest(context, webSiteUrl);
                else if(webSiteUrl.contains("www.instagram.com/"))
                    openInstagram(context, webSiteUrl);
                else if(webSiteUrl.contains("blogspot.com/"))
                    openBlogerApp(context, webSiteUrl);
                else if(webSiteUrl.contains("play.google.com"))
                    openAppRating(context, webSiteUrl);
                else
                    openAppselectorDialog(context, webSiteUrl);
            }

        }catch (Exception e){
            LibraryLogger.log("" + e, context);
        }


    }


    /**
     * Created by amitrai
     *
     * checks if facebook is installed then opens the page in facebook.
     * @param context
     * @param webSiteUrl
     */
    public static void openFacebookApp(Activity context, String webSiteUrl){
        try {
            List<String> facbook_check_list = new ArrayList<>();
            facbook_check_list.add("com.facebook.android");
            facbook_check_list.add("com.facebook.katana");

            Intent intent = isAppInstalled(context, facbook_check_list);
            Log.e("intent", "" + intent);
            if(intent != null){
                int versionCode = 0;
                try {
                    versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + webSiteUrl);
                        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                        String page = "fb://page/"+webSiteUrl.split("https://www.facebook.com/")[1];
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+page)));
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    openAppselectorDialog(context, webSiteUrl);
                }
            }else {
                openAppselectorDialog(context, webSiteUrl);
            }

        }catch (Exception e){
            LibraryLogger.log("" + e, context);
        }
    }




    /**
     * Created by amitrai
     *
     * opnens the link in twitter application
     * @param context
     * @param webSiteUrl
     */
    public static void openTwitterApp(Activity context, String webSiteUrl){
        try {
            Intent intent = null;
            List<String> twitter_check_list = new ArrayList<>();
            twitter_check_list.add("com.twitter.android");

            intent = isAppInstalled(context, twitter_check_list);
            Log.e("intent", "" + intent);
            if(intent != null){
                String page_name = null;
                if(webSiteUrl.contains("https://twitter.com/@"))
                    page_name = webSiteUrl.split("https://twitter.com/@")[1];
                else
                    page_name = webSiteUrl.split("https://twitter.com/")[1];
                // get the Twitter app if possible
                context.getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+page_name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else{
                openAppselectorDialog(context, webSiteUrl);
            }
        } catch (Exception e) {
            // no Twitter app, revert to browser
            LibraryLogger.log(""+e,context);
            openAppselectorDialog(context, webSiteUrl);
        }
    }


    /**
     * Created by amitrai
     *
     * opens the page in google plus
     * @param context
     * @param webSiteUrl
     */
    public static void openGPlus(Activity context , String webSiteUrl) {

        try {
            Intent intent = null;
            List<String> google_check_list = new ArrayList<>();
            google_check_list.add("com.google.android");
            intent = isAppInstalled(context, google_check_list);
            Log.e("intent", "" + intent);
            if(intent != null){

                intent = new Intent( Intent.ACTION_VIEW, Uri.parse( webSiteUrl ) );
                intent.setPackage("com.google.android.apps.plus");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }else
                    openAppselectorDialog(context, webSiteUrl);
            }else{
                openAppselectorDialog(context, webSiteUrl);
            }

        }catch (Exception e){
            LibraryLogger.log("" + e, context);
        }
    }




    /**
     * Created by amitrai
     *
     * opens the page in Linkedin
     * todo open the company page in linkedin app
     * @param context
     * @param webSiteUrl
     */
    public static void openLinkedin(Activity context , String webSiteUrl) {


//        openPageInStack(context, webSiteUrl);

        Uri webpage = Uri.parse(webSiteUrl);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(webIntent);

//        Intent intent = null;
//        List<String> google_check_list = new ArrayList<>();
//        google_check_list.add("com.linkedin.android");
//        intent = AppUtil.isAppInstalled(context, google_check_list);
//        StakBrowserLogger.log("intent", "" + intent);
//        if(intent != null){
//
//
//            try {
//                String page_name = webSiteUrl.split("https://www.linkedin.com/profile/")[1];
//                context.getPackageManager().getPackageInfo("com.linkedin.android", 0);
////                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://you"));
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://profile/"+page_name));
//
//                context.startActivity(intent);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//                openPageInStack(context, webSiteUrl);
//            }
////
////            intent = new Intent( Intent.ACTION_VIEW, Uri.parse( webSiteUrl ) );
////            intent.setPackage( "com.linkedin.android" );
////            if (intent.resolveActivity(context.getPackageManager()) != null) {
////                context.startActivity(intent);
////            }else
////                openPageInStack(context, webSiteUrl);
//        }else{
//            openPageInStack(context, webSiteUrl);
//        }
    }

    /**
     * Created by amitrai
     *
     * opens the page in youtube app
     * @param context
     * @param webSiteUrl
     *
     */
    public static void openYouTube(Activity context , String webSiteUrl) {

        try {

            Intent intent = null;
            if(YouTubeIntents.isYouTubeInstalled(context)){
                String name = null;
                if(webSiteUrl.contains("https://www.youtube.com/page/")){
                    name = webSiteUrl.split("https://www.youtube.com/page/")[1];
                    intent = YouTubeIntents.createChannelIntent(context, name);
                }else if(webSiteUrl.contains("https://www.youtube.com/user/")){
                    name = webSiteUrl.split("https://www.youtube.com/user/")[1];
                    intent = YouTubeIntents.createUserIntent(context, name);
                }
                context.startActivity(intent);

            }else
                openAppselectorDialog(context, webSiteUrl);
        }catch (Exception e){
            LibraryLogger.log("" + e, context);
        }
    }


    /**
     * Created by amitrai
     *
     * // todo still not working please fix
     * opens the page in youtube app
     * @param context
     * @param webSiteUrl
     */
    public static void openPlayStore(Activity context , String webSiteUrl) {
        String appid =  webSiteUrl.split("id=")[1];
        Uri uri = Uri.parse("market://details?id=" + webSiteUrl.split("id=")[1]);
        Intent intent = null;//new Intent(Intent.ACTION_VIEW, uri);
        List<String> google_check_list = new ArrayList<>();
        google_check_list.add("com.android.vending");
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appid)).setPackage("com.android.vending"));
        } catch (android.content.ActivityNotFoundException anfe) {
            openAppselectorDialog(context, webSiteUrl);
        }
    }

    /**
     * Created by amitrai
     *
     * opens blogger app
     * todo under progress
     * @param context
     * @param webSiteUrl
     */
    public static void openBlogerApp(Activity context , String webSiteUrl) {
        Uri webpage = Uri.parse(webSiteUrl);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(webIntent);
    }


    /**
     * Created by amitrai
     *
     * opens google play app
     * @param context
     * @param webSiteUrl
     */
    public static void openAppRating(Activity context, String webSiteUrl) {
        String appid =  webSiteUrl.split("id=")[1];
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appid));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;


            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appid));
            context.startActivity(webIntent);
        }
    }

    /**
     * Created by amitrai
     *
     * opens pinterest app if available or opens the app selector dialog
     */
    public static void openPinterest(Activity context, String webSiteUrl){
        try {
            String url = String.format(webSiteUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            filterByPackageName(context, intent, "com.pinterest");

            context.startActivity(intent);
        }catch (Exception e){
            LibraryLogger.log(""+e,context);
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(webSiteUrl)));
        }
    }

    /**
     * Created by amitrai
     *
     * checks if app is available or not
     * @param context
     * @param intent
     * @param prefix
     */
    public static void filterByPackageName(Activity context, Intent intent, String prefix) {
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)) {
                intent.setPackage(info.activityInfo.packageName);
                return;
            }
        }
    }


    /**
     * Created by amitrai
     *
     * opens link in instagram app
     * @param context
     * @param webSiteUrl
     */
    public static void openInstagram(Activity context, String webSiteUrl){
        Uri uri = Uri.parse(webSiteUrl);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(context, insta)){
            context.startActivity(insta);
        } else{
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webSiteUrl)));
        }

    }


    /**
     * Created by amitrai
     *
     * returns true/false flag if intent is available
     * @param ctx
     * @param intent
     * @return
     */
    private static boolean isIntentAvailable(Activity ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * Created by amitrai
     *
     * opens app selector dialog
     * @param context
     * @param webSiteUrl
     */
    public static void openAppselectorDialog(final Activity context, String webSiteUrl){
        Uri webpage = Uri.parse(webSiteUrl);
        final Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        Toast.makeText(context, "app not found", Toast.LENGTH_LONG).show();
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                context.startActivity(webIntent);
            }
        });
    }


    /**
     * Created by amitrai
     *
     * returns true / false if corresponding app is installed.
     * @param activity_instance
     * @param URL
     * @return
     */
    public static boolean canResolve(Activity activity_instance, String URL){
        List<String> list_package = new ArrayList<String>();

        if(activity_instance !=null && URL != null && URL.trim().length()>0){
            if(URL.contains("www.youtube.com")){
                list_package.clear();
                list_package.add("com.youtube.android");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("com.twitter.android")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("www.facebook.com/")){
                list_package.clear();
                list_package.add("com.facebook.android");
                list_package.add("com.facebook.katana");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("blogspot.com/")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("www.linkedin.com/")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("plus.google.com/")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("www.pinterest.com/")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("www.instagram.com/")){
                list_package.clear();
                list_package.add("com.instagram.android");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("blogspot.com/")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else if(URL.contains("play.google.com")){
                list_package.clear();
                list_package.add("");
                return (isAppInstalled(activity_instance, list_package) == null) ? false : true;
            }
            else
                return false;
        }
        return false;
    }



}
