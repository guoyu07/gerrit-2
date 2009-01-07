// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.client.changes;

import com.google.gerrit.client.reviewdb.ChangeMessage;
import com.google.gerrit.client.ui.DomUtil;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class MessagePanel extends Composite {
  boolean isRecent;

  public MessagePanel(final ChangeMessage msg) {
    final HTML l =
        new HTML(DomUtil.linkify(DomUtil.escape(msg.getMessage().trim())));
    l.setStyleName("gerrit-ChangeMessage-Message");
    initWidget(l);
  }
}
