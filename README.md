# SHOP
***
## Description
此專案目的是練習Redis與分散式系統的應用並記錄所學內容。

透過Redis與分散式系統，實作一個簡易的後端商店系統。

***
已實現功能：

- Redis
  - Cache Penetration (快取穿透)
    - 透過存入有時限的 `null` 值，避免資料庫查詢
  - Cache Avalanche (快取雪崩)
    - TTL增加隨機時間，避免同時過期，導致大量資料庫查詢
  - Cache Hotspot Invalid (快取擊穿)
    - 使用互斥鎖，限制同時只有一個Thread進行資料庫查詢
    - 由後端自行放入過期時間，過期後只放行一個Thread進行資料庫查詢，其餘Thread返回舊資料。（資料庫與Cache很要求一致性的情況下不可用）

- 分散式系統
  - Global ID

- 預計實現功能：
  - Distributed Lock