# セットアップ
## 1. 準備
### Homebrew
パッケージ管理システム
  * インストール
```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

### JDK
Javaの開発・動作に必要な開発キット
  * インストール
```bash
brew cask install java
```
  * JDK のインストールしたパスを確認し、環境変数に追加する
```
ls -l /Library/Java/JavaVirtualMachines/
```
```
cd ~
vi .bash_profile
```
  * `i`で入力モードに変更し、下記を追記して`:wq`
※追加するパスは先ほど確認したディレクトリ(バージョン)を記載する
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-14.0.1.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```

### STS(Spring ToolSuite)
Spring専用の統合開発環境
  * [STSダウンロード](https://spring.io/tools)
ダウンロードしたdmgファイルを実行し、中にあるSTS.appをこのApplicationsにコピーしてください。
  * [日本語化プラグインダウンロード](https://mergedoc.osdn.jp)
setup.appを実行し、STS.appを選択

### Docker
Dockerは非常に軽量なコンテナ型のアプリケーション実行環境  
 [ダウンロード](https://www.docker.com/docker-mac)
### Github
 [github.com](https://github.com/)
  * 新規リポジトリを作成する。(アカウントを持っていなければ場合は登録する。)
  * 作成したリポジトリの `Settings` > `Manage access` `Invite a collaborator` から研修担当者のGithubアカウントを追加する。
  * Slack研修用チャンネルに作成したリポジトリの通知がくるように連携を行う。<br>
    `/github subscribe yamada-t/shopping reviews`<br>
    認証を要求してくるので手順に従い設定する。
* Gitのユーザー設定<br>
```base
git config --global user.name "yamada-t"
git config --global user.email "yamada-t@company.co.jp"
```
### Oracle 
データベース管理システム
 * [18.3XEダウンロード](https://www.oracle.com/technetwork/jp/database/database-technologies/express-edition/downloads/xe-prior-releases-5172097-ja.html)
<!--
### DB作成〜起動

```
cd ../bin 
./1-sysdba.sh
./init-db.sh
```
(失敗したら、時間を置いて、もう一度実行)
```
CREATE TABLESPACE my_data DATAFILE '/u01/app/oracle/oradata/MY_DATA.dbf' SIZE 200M  SEGMENT SPACE MANAGEMENT AUTO;
CREATE USER testuser IDENTIFIED BY "DB_USER_PASSWORD" DEFAULT TABLESPACE my_data TEMPORARY TABLESPACE temp;
GRANT DBA TO testuser ;
quit;
```

```
./sqlplus.sh
```

```
create table STAFF (
    EMP_ID     number primary key,
    STAFF_NAME varchar2(100)
);
insert into STAFF (EMP_ID, STAFF_NAME) values (1, 'TakamotoSan');
commit;
exit
```

```
./clean-build.sh
```

```
./up.sh
```

```
localhost:8080/query
```

!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!
-->
## 2. 研修アプリの取得と作業用リポジトリへプッシュ
研修アプリを取得した後、自分の作業用リポジトリへプッシュを行う。
```bash
# 研修用リポジトリをローカルにshoppingというディレクトリ名でクローン
git clone https://github.com/takamoto-s/java-shopping-template shopping
# shoppingへ移動
cd shopping
# originの再設定
# <URL> は作成した自分のリポジトリの "HTTPS" を使用する
git remote set-url origin <URL>
# Githubにローカルリポジトリをプッシュ
git push origin master
```
## 3. masterブランチのプロテクションルール設定
Githubでmasterへのマージをレビュー必須とする[設定](https://drive.google.com/drive/folders/1jwtMsaLBwvPpkmjvfqIdrkwqHWQXjq7k?usp=sharing)を行う。
`.github/CODEOWNERS`に指定したGithubアカウントのレビュー承認を受けなければマージできなくなる。
## 4. ダウンロード済みのOracle XE の移動
```bash
cd docker/oracle/18.3.0/
cp ~/Downloads/LINUX.X64_180000_db_home.zip ./
```
## 5. dockerイメージのビルド
```bash
../buildDockerImage.sh -v 18.3.0 -x
```
## 6. Dockerコンテナ(DB)の起動
```bash
cd bin
./up-d.sh
```
## 7. Oracleのセットアップ
```bash
docker exec -it docker_dbserver_1 bash
sqlplus / as sysdba
# show con_name
alter session set container=PDB$SEED;
# select file_name from dba_data_files;
create pluggable database test admin user tuser identified by tpassword file_name_convert = ('/opt/oracle/oradata/XE/pdbseed/', '/opt/oracle/oradata/XE/test/');
! ls -l /opt/oracle/oradata/XE/test/
show pdbs
alter pluggable database TEST open;
```
## 8.DB初期データの投入
Flywayで行うため、不要

## 9. Dockerコンテナ(アプリ)の起動
```bash
# アプリのビルド
./clean-build.sh
# コンテナの起動
./up.sh
```
## 10. 動作確認
[http://localhost](http://localhost:8080) にアクセスして画面が表示されれば完了。

# 研修アプリケーションについての説明
<!--
```
~/shopping
├── laradock     # Laradockディレクトリ(docker~系のコマンドはここで実行)
├── .laradock    # データディレクトリ(MySQLのデータベースはここに保存)
└── application  # プロジェクトディレクトリ(機能追加はここに対して行う)
```
-->
## サービス起動/停止＋
```bash
# ビルド
~/bin/clean-build.sh
# 起動
~/bin/up.sh
# 停止(未作成)
~/bin/down.sh
# DBアクセス
~/bin/sqlplus.sh
```
# 課題
* フロントサイド
  * 認証
    * ログイン
    * ログアウト
    * パスワードリセット
    * 新規登録(イメージ以外)
* 管理サイド
  * ログイン
  * ログアウト
認証用テストユーザ
||ユーザ名|パスワード|
|---|---|---|
|フロントサイド|user@a.com|pass|
|管理サイド|admin@a.com|pass|
上記の実装済み機能以外について [設計書](https://drive.google.com/drive/folders/1VRGeN6YdkE5EmyPEBiIkk0y2TneF3RH2?usp=sharing) を参考に実装を行う。
管理サイドから実装するのがおすすめ。
ブランチモデルは [GitHub Flow](https://tracpath.com/bootcamp/learning_git_github_flow.html) を利用する。
ブランチ名は `topic/product_management_20200101` のようにする。
PullRequestは機能単位(商品管理、商品カテゴリ管理...)とする。
masterへのマージはGitHubでPullRequestを利用し、有識者のコードレビュー承認後にマージすること。
# リファレンスなど
[Spring](hhttps://spring.pleiades.io)
[Java](https://kazurof.github.io/GoogleJavaStyle-ja/)
[Bootstrap4.4](https://getbootstrap.com/docs/4.4/getting-started/introduction/)
[FontAwesome](https://fontawesome.com/)
[Git](https://git-scm.com/book/ja/v2)
[設計書の例](https://pm-rasinban.com/bd-write)
[テスト設計書](https://docs.google.com/spreadsheets/d/1eAcfaLHgvd0X8Bomp7Be1qAVlkXLonyHEdmTw5qP91s/edit?usp=sharing)
