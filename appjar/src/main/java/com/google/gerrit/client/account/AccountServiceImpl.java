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

package com.google.gerrit.client.account;

import com.google.gerrit.client.reviewdb.Account;
import com.google.gerrit.client.reviewdb.AccountSshKey;
import com.google.gerrit.client.reviewdb.ReviewDb;
import com.google.gerrit.client.rpc.BaseServiceImplementation;
import com.google.gerrit.client.rpc.NoSuchEntityException;
import com.google.gerrit.client.rpc.RpcUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtjsonrpc.client.VoidResult;
import com.google.gwtorm.client.OrmException;
import com.google.gwtorm.client.SchemaFactory;

import java.util.Collections;

public class AccountServiceImpl extends BaseServiceImplementation implements
    AccountService {
  public AccountServiceImpl(final SchemaFactory<ReviewDb> rdf) {
    super(rdf);
  }

  public void myAccount(final AsyncCallback<Account> callback) {
    run(callback, new Action<Account>() {
      public Account run(ReviewDb db) throws OrmException {
        return db.accounts().byId(RpcUtil.getAccountId());
      }
    });
  }

  public void mySshKeys(final AsyncCallback<SshKeyList> callback) {
    run(callback, new Action<SshKeyList>() {
      public SshKeyList run(ReviewDb db) throws OrmException {
        return new SshKeyList(db.accountSshKeys().byAccount(
            RpcUtil.getAccountId()).toList());
      }
    });
  }

  public void addSshKey(final String keyText,
      final AsyncCallback<AccountSshKey> callback) {
    run(callback, new Action<AccountSshKey>() {
      public AccountSshKey run(final ReviewDb db) throws OrmException {
        int max = 0;
        final Account.Id me = RpcUtil.getAccountId();
        for (final AccountSshKey k : db.accountSshKeys().byAccount(me)) {
          max = Math.max(max, k.getKey().get());
        }

        final AccountSshKey newKey =
            new AccountSshKey(new AccountSshKey.Id(me, max + 1), keyText);
        db.accountSshKeys().insert(Collections.singleton(newKey));
        return newKey;
      }
    });
  }

  public void deleteSshKey(final AccountSshKey.Id id,
      final AsyncCallback<VoidResult> callback) {
    run(callback, new Action<VoidResult>() {
      public VoidResult run(final ReviewDb db) throws OrmException, Failure {
        final Account.Id me = RpcUtil.getAccountId();
        if (!me.equals(id.getParentKey()))
          throw new Failure(new NoSuchEntityException());

        final AccountSshKey k = db.accountSshKeys().get(id);
        if (k != null) db.accountSshKeys().delete(Collections.singleton(k));

        return VoidResult.INSTANCE;
      }
    });
  }
}
