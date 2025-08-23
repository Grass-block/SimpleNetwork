<div align="center" id="readme-top">

<h2 align="center">SimpleNetwork</h2>

A simple netty utility library.

[Explore docs](https://wiki.atforever.world/quark/) | [Report issue](https://github.com/Grass-block/Quark-Plugin/issues)

![MCVersion](https://img.shields.io/badge/netty-4.1.100-3366CC?style=for-the-badge&logoColor=blue&labelColor=29355F)
![Java17+](https://img.shields.io/badge/java-17+-009B98?style=for-the-badge&logoColor=blue&labelColor=29355F)
![Opensource](https://img.shields.io/badge/OpenSource-MIT-AD3333?style=for-the-badge&logoColor=blue&labelColor=29355F)

</div>

## Description

- simpnet-base: basic systems(packets & simple codecs) for netty pipeline. (WIP)
- simpnet-http: a lightweight HTTP server based on sun.http. (WIP)
- simpnet-connector: connector abstractions. (WIP)
- simpnet-jkcp: jkcp-implemented connector. (WIP)


## How to use?

- clone the full repo.
- load project in ur computer
- click build on sub-moules you want to use
- wait and get well-done libraries from productions folder.
- enjoy :D

## GBuild.gradle

GBuild is a re-useable general script to enhance gradle experience.
Before initializing the project, please SET the `GBUILD_WORKSPACE`:

```groovy
def GBUILD_WORKSPACE = "E:/Java/GBuild" //change to the dest you want
```

GBuild must work with this workspace exist, and it will always attempt to publish
productions to workspace unless you remove `publish_gbuild` task.
gbuild-imported library can work without existing in workspace.
It is always downloaded and updated in project local directory.