NEVIOS REDSTONE OPTIMIZER  (Fabric, Minecraft 1.21.11, nur Client)
==================================================================

WAS MACHT DIE MOD?
- GUI per Taste Ü (in den Steuerungs-Einstellungen änderbar).
- Pro Bauteil schaltbar: Observer, Werfer, Spender, Repeater, Komparator, Redstone-Staub,
  Redstone-Fackel, Kolben-Animation, Kolben, Redstone-Block, Lampe, Trichter, Hebel, Zielblock, Schienen.
- Aktivierte Bauteile werden nicht mehr gerendert, erzeugen keine Staub-/Rauchpartikel,
  und Zustandswechsel (an/aus) lösen keine Chunk-Neuberechnung mehr aus (das ist der größte Lag-Fresser).
- Einstellungen: config/nevios_redstone_optimizer.json

BAUEN (Java 21 + Gradle nötig):
1. Am einfachsten: auf https://fabricmc.net/develop/template/ ein Template für 1.21.11 erzeugen
   (Mappings: "Mojang"), entpacken.
2. Den Ordner "src" aus diesem Projekt in das Template kopieren (vorhandenen src ersetzen).
3. In der Konsole:  ./gradlew build     (Windows: gradlew.bat build)
4. Fertige Mod: build/libs/nevios-redstone-optimizer-1.0.0.jar  (NICHT die "-sources.jar")
   -> in den mods-Ordner legen, Fabric Loader + Fabric API für 1.21.11 werden benötigt.

Alternativ: build.gradle / gradle.properties / settings.gradle aus diesem Projekt verwenden,
dann fehlt nur noch der Gradle-Wrapper (gradle wrapper).

HINWEIS: Diese Mod konnte beim Erstellen nicht kompiliert/getestet werden.
Falls das Spiel Fehler meldet: Datei logs/latest.log suchen und Zeilen mit "mixin" oder "Nevios" kopieren.
