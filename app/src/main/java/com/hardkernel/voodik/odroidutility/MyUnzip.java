package com.hardkernel.voodik.odroidutility;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.org.apache.commons.codec.digest.DigestUtils;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MyUnzip extends AsyncTask<Void, Integer, Long> {

    private static final String TAG = Constants.TAG;
    private WeakReference<Context> mWeakContext;
    private Uri _zipFile;
    private Uri _md5sumFile;
    private String _location;
    private ProgressDialog myProgressDialog;

    MyUnzip(WeakReference<Context> c, Uri zipFilePath, Uri md5sumFilePath, String destDir) {
        super();
        this.mWeakContext = c;
        this._zipFile = zipFilePath;
        this._md5sumFile = md5sumFilePath;
        this._location = destDir;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgressDialog = new ProgressDialog(mWeakContext.get());
        myProgressDialog.setMessage("Please Wait... Unzipping");
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
    }


    @Override
    protected Long doInBackground(Void... params){
        long filesize = 0;
        File dir = new File(_location);
        long extractedSize = 0;
        final Context ctx = mWeakContext.get();

        // create output directory if it doesn't exist



        if (!dir.exists()) dir.mkdirs();
        try {

            filesize = getfilesizefromuri(_zipFile, ctx);
            System.out.println("getfilesizefromuri " + filesize);
            InputStream mdinputStream = ctx.getContentResolver().openInputStream(_md5sumFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(mdinputStream));
            String mdline = reader.readLine();
            mdline = mdline.trim();
            mdline = mdline.replaceAll("\\s{2,}", " ");
            String mdsum = mdline.split(" ")[0].toUpperCase();
            reader.close();
            mdinputStream.close();


            InputStream inputStream = ctx.getContentResolver().openInputStream(_zipFile);
//            BufferedInputStream fis = new BufferedInputStream(inputStream);
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");

            System.out.println("MD5 " + mdsum);
            myProgressDialog.setMessage("Please Wait... Checking MD5 checksum");
            String md5 = DigestUtils.md5Hex(inputStream);
            System.out.println("MD5 " + md5.toUpperCase());
//            fis.close();
            inputStream.close();

            inputStream = ctx.getContentResolver().openInputStream(_zipFile);
            BufferedInputStream fis = new BufferedInputStream(inputStream);


            //buffer for read and write data to file
            byte[] buffer = new byte[1024];
            try {
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze = zis.getNextEntry();
                publishProgress(0);
                myProgressDialog.setMessage("Please Wait... Unzipping update.zip");
                while (ze != null) {
                    String fileName = ze.getName();
                    extractedSize += ze.getCompressedSize();
                    publishProgress((int)((extractedSize * 100) / filesize));
                    myProgressDialog.setMessage("Please Wait... Unzipping " +fileName);
                    File newFile = new File(_location + File.separator + fileName);
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int len;

                    while ((len = zis.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    bos.close();
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
                //close last ZipEntry
                zis.closeEntry();
                zis.close();
                fis.close();
                publishProgress(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("MNG Unzip", "unzip done");
    return extractedSize;
    }

    @Override
    protected void  onPostExecute(Long result) {
        Log.i(TAG, "Completed. Total size: "+result);
        if(myProgressDialog != null && myProgressDialog.isShowing()){
            myProgressDialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        myProgressDialog.setProgress(progress[0]); //Since it's an inner class, Bar should be able to be called directly
    }

    private String getFileChecksum(MessageDigest digest, InputStream is, Long filesize) throws IOException
    {

        String md5 = DigestUtils.md5Hex(is);
        //return complete hash
        return md5.toUpperCase();
    }

    private Long getfilesizefromuri(Uri uri, Context c) {
        long filesize = 0;
        String url = uri.toString();
        if (url.contains("content://")){
            Cursor returnCursor =
                    c.getContentResolver().query(uri, null, null, null, null);

            returnCursor.moveToFirst();
            filesize = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE));
            returnCursor.close();
        } else{
            File tmp = new File(url.replaceAll("file://",""));
            filesize = tmp.length();
        }

        return filesize;
    }

}
