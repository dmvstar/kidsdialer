/* Copyright (c) 2014, Dmitry Starzhynskyi
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sf.dvstar.kidsdialer.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import net.sf.dvstar.kidsdialer.utils.Log;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
    private Context ctx;
	private final ProgressDialog dialog = new ProgressDialog(ctx);

	public ExportDatabaseFileTask(Context ctx) {
		this.ctx = ctx;
	}
	
    // can use UI thread here
    protected void onPreExecute() {
       this.dialog.setMessage("Exporting database...");
       this.dialog.show();
    }

    // automatically done on worker thread (separate from UI thread)
    protected Boolean doInBackground(final String... args) {

       File dbFile =
                new File(Environment.getDataDirectory() + "/data/com.mypkg/databases/mydbfile.db");

       File exportDir = new File(Environment.getExternalStorageDirectory(), "");
       if (!exportDir.exists()) {
          exportDir.mkdirs();
       }
       File file = new File(exportDir, dbFile.getName());

       try {
          file.createNewFile();
          this.copyFile(dbFile, file);
          return true;
       } catch (IOException e) {
          Log.e( e.getMessage(), e);
          return false;
       }
    }

    // can use UI thread here
    protected void onPostExecute(final Boolean success) {
       if (this.dialog.isShowing()) {
          this.dialog.dismiss();
       }
       if (success) {
          Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
       } else {
          Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
       }
    }

    void copyFile(File src, File dst) throws IOException {
       FileChannel inChannel = new FileInputStream(src).getChannel();
       FileChannel outChannel = new FileOutputStream(dst).getChannel();
       try {
          inChannel.transferTo(0, inChannel.size(), outChannel);
       } finally {
          if (inChannel != null)
             inChannel.close();
          if (outChannel != null)
             outChannel.close();
       }
    }

 }