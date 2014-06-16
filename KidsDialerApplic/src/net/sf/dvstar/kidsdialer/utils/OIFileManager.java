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

package net.sf.dvstar.kidsdialer.utils;

public interface OIFileManager {


		/**
		 * Activity Action: Pick a file through the file manager, or let user
		 * specify a custom file name.
		 * Data is the current file name or file name suggestion.
		 * Returns a new file name as file URI in data.
		 * 
		 * <p>Constant Value: "org.openintents.action.PICK_FILE"</p>
		 */
		public static final String ACTION_PICK_FILE = "org.openintents.action.PICK_FILE";

		/**
		 * Activity Action: Pick a directory through the file manager, or let user
		 * specify a custom file name.
		 * Data is the current directory name or directory name suggestion.
		 * Returns a new directory name as file URI in data.
		 * 
		 * <p>Constant Value: "org.openintents.action.PICK_DIRECTORY"</p>
		 */
		public static final String ACTION_PICK_DIRECTORY = "org.openintents.action.PICK_DIRECTORY";
		
		/**
		 * Activity Action: Move, copy or delete after select entries.
	     * Data is the current directory name or directory name suggestion.
	     * 
	     * <p>Constant Value: "org.openintents.action.MULTI_SELECT"</p>
		 */
		public static final String ACTION_MULTI_SELECT = "org.openintents.action.MULTI_SELECT";

		public static final String ACTION_SEARCH_STARTED = "org.openintents.action.SEARCH_STARTED";
		
		public static final String ACTION_SEARCH_FINISHED = "org.openintens.action.SEARCH_FINISHED";
		
		/**
		 * The title to display.
		 * 
		 * <p>This is shown in the title bar of the file manager.</p>
		 * 
		 * <p>Constant Value: "org.openintents.extra.TITLE"</p>
		 */
		public static final String EXTRA_TITLE = "org.openintents.extra.TITLE";

		/**
		 * The text on the button to display.
		 * 
		 * <p>Depending on the use, it makes sense to set this to "Open" or "Save".</p>
		 * 
		 * <p>Constant Value: "org.openintents.extra.BUTTON_TEXT"</p>
		 */
		public static final String EXTRA_BUTTON_TEXT = "org.openintents.extra.BUTTON_TEXT";

		/**
		 * Flag indicating to show only writeable files and folders.
	     *
		 * <p>Constant Value: "org.openintents.extra.WRITEABLE_ONLY"</p>
		 */
		public static final String EXTRA_WRITEABLE_ONLY = "org.openintents.extra.WRITEABLE_ONLY";

		/**
		 * The path to prioritize in search. Usually denotes the path the user was on when the search was initiated.
		 * 
	     * <p>Constant Value: "org.openintents.extra.SEARCH_INIT_PATH"</p>
		 */
		public static final String EXTRA_SEARCH_INIT_PATH = "org.openintents.extra.SEARCH_INIT_PATH";

		/**
		 * The search query as sent to SearchService.
		 * 
	     * <p>Constant Value: "org.openintents.extra.SEARCH_QUERY"</p>
		 */
		public static final String EXTRA_SEARCH_QUERY = "org.openintents.extra.SEARCH_QUERY";

		/**
	     * <p>Constant Value: "org.openintents.extra.DIR_PATH"</p>
		 */
		public static final String EXTRA_DIR_PATH = "org.openintents.extra.DIR_PATH";

		/**
		 * Extension by which to filter.
		 * 
	     * <p>Constant Value: "org.openintents.extra.FILTER_FILETYPE"</p>
		 */
		public static final String EXTRA_FILTER_FILETYPE = "org.openintents.extra.FILTER_FILETYPE";
		
		/**
		 * Mimetype by which to filter.
		 * 
	     * <p>Constant Value: "org.openintents.extra.FILTER_MIMETYPE"</p>
		 */
		public static final String EXTRA_FILTER_MIMETYPE = "org.openintents.extra.FILTER_MIMETYPE";
		
		/**
		 * Only show directories.
		 * 
	     * <p>Constant Value: "org.openintents.extra.DIRECTORIES_ONLY"</p>
		 */
		public static final String EXTRA_DIRECTORIES_ONLY = "org.openintents.extra.DIRECTORIES_ONLY";

		public static final String EXTRA_DIALOG_FILE_HOLDER = "org.openintents.extra.DIALOG_FILE";

		public static final String EXTRA_IS_GET_CONTENT_INITIATED = "org.openintents.extra.ENABLE_ACTIONS";

		public static final String EXTRA_FILENAME = "org.openintents.extra.FILENAME";
	

		public static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
		public static final int REQUEST_CODE_GET_CONTENT = 2;
		
		
		
		
	
}
