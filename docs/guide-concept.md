---
layout: global
title: Basic Concept
---

本节主要介绍Moonbox涉及到的一些基本概念,了解这些基本概念有助于理解Moonbox的设计思想和更好的使用Moonbox。

#### 数据虚拟化

数据虚拟化，是指对于用户或用户程序而言，面对的是统一的交互方式和查询语言，而无需关注数据实际所在的物理库和方言及交互方式（异构系统／异构查询语言）的一种技术。用户的使用体验是面对一个单一数据库进行操作，但其实这是一个虚拟化的数据库，数据本身并不存放于虚拟数据库中。

<p style="text-align: center;">
  <img src="img/data-virtualization.jpg" title="Data Virtualization" alt="Data Virtualization" />
</p>
<p style="text-align: center;">
  (选自"Designing a Modern Data Warehouse + Data Lake" - Melissa Coates, Solution Architect, BlueGranite)
</p>


数据虚拟化软件通常需要具备以下几个能力:
- 透明访问
    以统一的方式和语言访问不同的数据源,而不用关心数据源物理位置和特性。
- 多数据源支持
    需要将数据源的访问形式和查询语言进行抽象,支持多种异构数据源,同时能够很方便的进行扩展。
- 分布式并行计算
    可以以分布式计算方式快速进行跨多异构数据源进行混算
- 元数据获取
    可以通过统一的方式获取底层数据源的元数据信息
- 数据安全
    数据安全包括用户登录认证、授权访问、数据加密、操作审计等。认证和授权是针对用户而言的,加密是从数据本身考虑的,审计从系统角度角度出发的

Moonbox即定位于上图中的Data Virtualization Layer,并提供了如上所述的能力。让我们来了解一下Moonbox中与之相关的概念。

#### 数据源

#### 数据映射

#### 用户体系

#### 权限体系

#### 分布式计算

#### 下推优化

