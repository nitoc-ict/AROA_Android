# AROA_Android <!-- omit in toc -->

## 説明 <!-- omit in toc --> <br/>

AROAは、高専プロコン第33回群馬大会 課題部門提出作品です。<br/>
企画書 [AROA -拡張現実で日常に競争を-](https://drive.google.com/file/d/13uSCRXZHwz8V4P9N1hx53DmdcfdGsp91/view?usp=sharing)<br/>
<br/>
AROA_Androidは、AROAのAndoridアプリです。<br/>
AROAは、Andorid, WearOS, Nrial Air のアプリケーションで構成されます。<br/>
Andoridアプリは主な機能を担っています。<br/>
Androidアプリ単体でも動作しますが、WearOS と Nrial Air と連携させることで進化を発揮します。
<br/>
<br/>

## 関連リポジトリ <!-- omit in toc -->
[AROA_wearOS](https://github.com/nitoc-ict/AROA_wearOS)<br/>
[AROA_Unity](https://github.com/nitoc-ict/AROA_Unity)<br/>
<br/>
<br/>

## 目次 <!-- omit in toc --> <br/>
- [開発するにあたって](#開発するにあたって)
  - [各ブランチの説明](#各ブランチの説明)
  - [作業手順](#作業手順)
  - [環境構築](#環境構築)

<br/>
<br/>


# 開発するにあたって

## 各ブランチの説明

**main**<br/>
動く状態のアプリを置くようにしたい<br/>

**dev**<br/>
開発途中で機能を統合していくブランチ<br/>

**feature/**_hogefuga_<br/>
機能を開発する為のブランチ<br/>
<br/>
<br/>



## 作業手順
1. `git switch dev` でdevブランチに移動する。

1. `git switch -c ブランチ名` で、devブランチから新規ブランチを作成します。<br/>
ブランチ名のつけ方に関しては、 **ブランチ命名規則** を参照してください。

1. 作業をします。この際、コミットの粒度を細かくすることを意識してください。<br/>
詳しくは **コミット粒度について** を参照

1. 作業中に15分調べたり考えてもわからない事がある際は、<br/>
Draft Pull Requestを用いて、わからない箇所を聞くとコードを見ながら意見を出し合えます。<br/>
[Draft Pull Request の使い方](https://qiita.com/tatane616/items/13da1b6797a7b871ad58)<br/>
もし、この機能の使い方がわからない時は、プロジェクトの管理者に直接連絡して聞くとよいです。<br/>
優しく教えてくれます。

1. 作業が完了したら、**dev** ブランチにプルリクエストを投げます。<br/>
この際、チームリーダーや、リポジトリのオーナーをレビュワーに指定してください。<br/>
プルリクエストの投げ方は、 **プルリクエストの作成方法** を参照してください。<br/>
この際、コードレビューをする人は[この記事](https://qiita.com/hinora/items/266fbfb62fd3d4c45edd)を熟読している事が推奨されています。

1. レビュワーのコードレビューを待ちます。<br/>
レビューが帰ってきたら、レビュワーの指示に従い、コードの修正またはマージを行ってください。<br/>
この際、コードレビューを受ける人は[この記事](https://qiita.com/hinora/items/fb083a97d6e2ab8a9aa3)を熟読している事が推奨されています。

1. コードレビューが完了し、無事にマージされたら基本的にはブランチは削除して構いません。<br/>
別途、管理者の指示がある場合はそれに従いましょう。
<br/>
<br/>

## 環境構築
本プロジェクトは、以下の環境を想定しています。<br/>

| ツール名 | バージョン |
| :--- | ---: |
| AndroidStudio | Chipmunk 2021.2.1 Patch 2 |
| Git | 2.37.1.windows.1 |
| JDK | 1.8 |

また、各種設定は済んでいるものとします。

1. `git clone https://github.com/nitoc-ict/AROA_Android.git` または<br/>
 `git clone git@github.com:nitoc-ict/AROA_Android.git` でリポジトリをクローンします。
<br/>
<br/>

今の所はこれだけで完了です。<br/>
**API_KEYを記入したファイルなどが必要になったら追記してください。**


