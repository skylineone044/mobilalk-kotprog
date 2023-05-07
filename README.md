# mobilalk-kotprog
19 - Számítástechnikai eszköz webshop

## ha valami nem megy: `skyline#0269`

## Mi merre:

- Fordítási hiba nincs
    nekem megy
- Futtatási hiba nincs
    nekem megy
- Firebase autentikáció meg van valósítva; Be lehet jelentkezni és regisztrálni:
    - Vendég bejelentkezés: `app/src/main/java/com/example/thechshop/LoginActivity.java:45`
    - Jelszavas bejelentkezés: `app/src/main/java/com/example/thechshop/LoginActivity.java:43`
    - Regisztráció: `app/src/main/java/com/example/thechshop/SignupActivity.java:46`
- Adatmodell definiálása (class vagy interfész formájában):
    - `app/src/main/java/com/example/thechshop/ShopItem.java`
    - `app/src/main/java/com/example/thechshop/CartItem.java`
- Legalább 3 különböző activity vagy fragmens használata
    - `app/src/main/java/com/example/thechshop/MainActivity.java`
    - `app/src/main/java/com/example/thechshop/ShopActivity.java`
    - `app/src/main/java/com/example/thechshop/CartActivity.java`
- Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.):
    ezekben vannak szöveges beviteli mezők, mindben jó típustt adtam meg:
    - `app/src/main/res/layout/activity_signup.xml`
    - `app/src/main/res/layout-land/activity_signup.xml`
    - `app/src/main/res/layout/activity_login.xml`
    - `app/src/main/res/layout-land/activity_login.xml`
- ConstraintLayout és még egy másik layout típus használata
    - ConstraintLayout `app/src/main/res/layout/activity_login.xml:2`
    - LinearLayout `app/src/main/res/layout/activity_login.xml:9`
- Reszponzív:
    - különböző kijelző méreteken is jól jelennek meg a GUI elemek (akár tableten is)
        - igekeztem relatív mérőszámokat használni, hogy ne csússzon szét
    - elforgatás esetén is igényes marad a layout
        - Amelyeknek szükséges volt, ott van külön landscape layout
            - `app/src/main/res/layout/activity_signup.xml`
            - `app/src/main/res/layout-land/activity_signup.xml`
            - `app/src/main/res/layout/activity_login.xml`
            - `app/src/main/res/layout-land/activity_login.xml`
            - `app/src/main/res/layout/activity_main.xml`
            - `app/src/main/res/layout-land/activity_main.xml`
            - `app/src/main/res/layout/activity_cart.xml`
            - `app/src/main/res/layout-land/activity_cart.xml`
- Legalább 2 különböző animáció használata
    - egyik: `app/src/main/res/anim/slide_anim1.xml`
      használata: `app/src/main/java/com/example/thechshop/ItemListAdapter.java:51`
    - másik: `app/src/main/res/anim/anim2.xml`
      használata: `app/src/main/java/com/example/thechshop/CartItemListAdapter.java:49`
- Intentek használata: navigáció meg van valósítva az activityk/fragmensek között (minden activity/fragmens elérhető):
    - `app/src/main/java/com/example/thechshop/MainActivity.java:36`
    - `app/src/main/java/com/example/thechshop/MainActivity.java:41`
    - `app/src/main/java/com/example/thechshop/MainActivity.java:59`
    - `app/src/main/java/com/example/thechshop/ShopActivity.java:84`
- Legalább egy Lifecycle Hook használata a teljes projektben: onCreate nem számít; az alkalmazás funkcionalitásába értelmes módon beágyazott, azaz pl. nem csak egy logolás:
    onResume van arra használva, hogy a a user be van jelentkezve és visszalépne a bejelentkezésre, akkor automatikusan visszadobom a ShopActivity-re, így megakadályozta a többszörös bejelentkezést és azoknak az összeakadását
    - `app/src/main/java/com/example/thechshop/MainActivity.java:64`
- Legalább egy olyan androidos erőforrás használata, amihez kell android permission
    -  nincs
- Legalább egy notification vagy alam manager vagy job scheduler használata
    - nincs
- CRUD műveletek mindegyike megvalósult és az adatbázis műveletek a konvenciónak megfelelően külön szálon történnek
    - CREATE: `app/src/main/java/com/example/thechshop/ShopActivity.java:116`
        Firebase Collection-ön keresztül atadfeltöltés a logkális alap adatokból, ha üres lenne az adatbázis
    - CREATE: `app/src/main/java/com/example/thechshop/ItemListAdapter.java:99`
        Firebase Collection-ön új tárgy felvétele a kosárba
    - READ: `app/src/main/java/com/example/thechshop/ShopActivity.java:98`
        A firebase adatbázisból betöltött item-ek lokális listába töltése
    - READ: `app/src/main/java/com/example/thechshop/CartActivity.java:75`
        A firebase adatbázisból betöltött kosár elemek lokális listába töltése
    - UPDATE: `app/src/main/java/com/example/thechshop/ItemListAdapter.java:89`
        A Cart kollekcióban ellenőrzöm, hogy adott féle tárgy van-e a felhasználó kosarában, ha van, akkor a számát növelem, ha nincs, akkor berakok egy új elemet a kosárba
    - DELETE: `app/src/main/java/com/example/thechshop/CartActivity.java:95`
        Ha a felhasználó leadja a megrendelést, a kosara kiürítésre kerül: az összes olyan kosár elem törlésre kerül, ami az adott user-hez tartozott
    - Mindegyik a standard firebase függvényekkel van megvalósítva, így már alapból mindegyik külőn szálon fut
- Legalább 2 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás):
    - `app/src/main/java/com/example/thechshop/ItemListAdapter.java:92`
        kettő where feltétel az adott tárgy és az adott felhasználóra
    - `app/src/main/java/com/example/thechshop/ShopActivity.java:96`
        limitálás: csak 10 elem betöltése
    - `app/src/main/java/com/example/thechshop/CartActivity.java:97`
        szűrés a felhasználóra
    - `app/src/main/java/com/example/thechshop/CartActivity.java:88`
        szűrés a tárgy nevére
- Szubjektív pontozás a projekt egészére vonatkozólag: ez 5-ről indul és le lehet vonni, ha igénytelen, összecsapott, látszik hogy nem foglalkozott vele, kísértetiesen hasonlít a videóban létrehozotthoz stb.
