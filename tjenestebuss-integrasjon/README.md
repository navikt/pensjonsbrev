Når du oppgraderer cxf-avhengighetene her, må du passe på å også oppdatere src/resources/META-INF/cxf/bus-extensions.txt.

Denne skal ha det samla innholdet fra bus-extensions.txt i alle cxf-avhengighetene. En grei måte å hente dem ut på er å bygge modulen, så kjøre unzip på hver modul som starter med `cxf-`, og så kopiere fra bus-extensions der. 