# セットアップ

## 1. 準備

* [Docker](https://www.docker.com/docker-mac)

  * Dockerは非常に軽量なコンテナ型のアプリケーション実行環境。

  * インストール済みの場合はスキップ。

* [Github](https://github.com/)

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

* IDEとして [PhpStorm](https://www.jetbrains.com/ja-jp/phpstorm/) がおすすめ。
  * 30日の試用が可能。

## 2. 研修アプリの取得と作業用リポジトリへプッシュ

研修アプリを取得した後、自分の作業用リポジトリへプッシュを行う。

```bash
# 研修用リポジトリをローカルにshoppingというディレクトリ名でクローン
git clone --recurse-submodules https://github.com/nakama-t/shopping-template shopping

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

## 4. Laradockのセットアップ

Dockerを利用してLaravel実行環境を構築してくれるLaradockを利用する。

Laradockの初期設定を行う。

```bash
# laradock初期設定を行うスクリプトを実行する
./init.sh
```

## 5. Dockerコンテナの起動

```bash
cd laradock

docker-compose up -d nginx mysql mailhog
```

初回起動はしばらく時間がかかります。

## 6. Laravelのセットアップ

```bash
# workspaceにログイン
docker-compose exec workspace bash

# 依存関係のインストール
composer install

# 環境ファイルのコピー
cp .env.example .env

# アプリケーションキーの生成
php artisan key:generate

# データベースのマイグレート&シードデータ投入
php artisan migrate --seed

# ストレージをリンク
php artisan storage:link

# 依存関係のインストール
yarn

# アセットファイルのコンパイル
yarn run dev

# workspaceからログアウト
exit
```

## 7. 動作確認

[http://localhost](http://localhost) にアクセスして画面が表示されれば完了。

## 8. PhpStorm補完設定

PhpStormを利用する場合、モデル修正の都度実行するとコーディング時の補完が適用される。

```bash
php artisan ide-helper:model -RW
```


# 研修アプリケーションについての説明

```
~/shopping
├── laradock     # Laradockディレクトリ(docker~系のコマンドはここで実行)
├── .laradock    # データディレクトリ(MySQLのデータベースはここに保存)
└── application  # プロジェクトディレクトリ(機能追加はここに対して行う)
```

## サービス起動/停止

```bash
# 起動
docker-compose up -d nginx mysql mailhog

# 停止
docker-compose down
```

## コマンド実行

composer, php artisan, yarn などのコマンドを実行する場合、環境はworkspaceコンテナ内にあるので

workspaceにログインする必要がある。

```bash
# コンテナにbashでログインする
docker-compose exec workspace bash

# 任意のコードを実行
php artisan migrate

# コンテナからログアウト
exit
```

ログインせず直接実行も可能。

```bash
docker-compose exec workspace php artisan migrate
```

## アセットコンパイル

js/sass のファイル編集した場合、コンパイルしなければ画面に反映されない。

```bash 
yarn run dev
```

もしくは変更を検知して自動でコンパイルすることもできる。

```bash
# Ctrl + C で終了
yarn run watch
```

`webpack.mix.js` にコンパイル対象の設定がある。

## データーベース(MySQL)

```bash
docker-compose exec mysql mysql -u default -p
```

コマンド実行後パスワードを聞かれるので "secret" と入力する。

## メール

[http://localhost:8025](http://localhost:8025) にアクセスするとアプリから送信されたメールを確認できる。


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

[Laravel 6](https://readouble.com/laravel/6.x/ja/installation.html)

[PHP](https://www.php.net/manual/ja/index.php)

[Bootstrap4.4](https://getbootstrap.com/docs/4.4/getting-started/introduction/)

[FontAwesome](https://fontawesome.com/)

[Git](https://git-scm.com/book/ja/v2)

[設計書の例](https://pm-rasinban.com/bd-write)

[テスト設計書](https://docs.google.com/spreadsheets/d/1eAcfaLHgvd0X8Bomp7Be1qAVlkXLonyHEdmTw5qP91s/edit?usp=sharing)
