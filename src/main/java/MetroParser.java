import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.List;

public class MetroParser {
    private StationIndex metro;
    private final String linkToWiki = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public StationIndex getStationIndex() throws IOException {
        metro = new StationIndex();
//        metro = parseStationIndexFromWiki();
//        parseConnectionFromWiki(metro);
        parseLineFromWiki(metro);
        parseStationFromWiki(metro);
        return metro;
    }

    private String getColorFromTdStyle(String styleAttribute) { //Метод, который парсит цвет из атрибута style у td
        String color;
        int firstSharpSymbol = 0; // Будем хранить первую #тут
        for (int i = 0; i < styleAttribute.length(); i++) { //Пробегамся по строке, ищем первый #
            if (styleAttribute.charAt(i) == '#') {
                firstSharpSymbol = i;
                break;
            }
        }
        if (firstSharpSymbol == 0)
            return "#FFD702"; //Т.к. один цвет у линии 8A парсится криво (не задан), придётся задать вручную
        //Остальные парсяться нормально
        color = styleAttribute.substring(firstSharpSymbol, firstSharpSymbol + 7);

        return color;
    }

    private String delAllExcessCharsOfStationName(String nameWithExcessChars) { //удаляет всё лишние из названии станции
        String stationName;
        stationName = nameWithExcessChars.replaceAll("/wiki/", "");
        //Сейчас станция имеет вид: Хорошёвская_(станция_метро) Будем удалять всё лишнее, начиная с _
        int numberOfEcessSymbol = 0; //это номер нижнего подчёркивания
        for (int i = 0; i < stationName.length(); i++) {
            if (stationName.charAt(i) == '_' && stationName.charAt(i + 1) == '(') {
                numberOfEcessSymbol = i;
                break;
            }
        }
        stationName = stationName.substring(0, numberOfEcessSymbol);
        stationName = stationName.replaceAll("_", " "); //Удаляем все _, они могли остаться в названиях, где 2 и более слов
        return stationName;
    }

    private Elements getTableLineFromWiki() throws IOException { //Получаем стройки таблиц, которые содержат инфо о станции в википедии
        Document document = Jsoup.connect(linkToWiki).maxBodySize(0).get(); //снимаем ограничение по размеру
        Elements tableLine = document.select("table[class=standard sortable]"); //Получаем все таблицы с классом standard sortable, именно в них лежат станции
        return tableLine.select("tbody").select("tr"); //Получаем все строки этих таблиц и ретёрним их
    }

    private void parseLineFromWiki(StationIndex stationIndexForParsing) throws IOException {
        Elements tableLine = getTableLineFromWiki();

        for (Element element : tableLine) { //Перебираем все строки таблиц, содержащих инфо о станциях
            Elements cells = element.select("td"); //Получаем элементы, содержащие ячейки каждой линии
            //Если ячеек 7 - это монорельс или Московское центральное кольцо
            //Если ячеек 8 - это обычное метро

            if (cells.size() == 7 || cells.size() == 8) { //Если в строке 7 или 8 ячеек то парсим инфу оттуда
                String lineNumber = cells.select("span.sortkey").first().text(); //Получаем номер линии
                String lineName = cells.get(0).select("img").attr("alt"); //Название линии
                lineName = lineName.replaceAll(" линия", ""); //Удаляем слово "линия"
                String lineColor;
                //Т.к. у 11A цвет не прописан, придётся задать его вручную
                if (lineNumber.equals("011А")) {
                    lineColor = "#82C0C0";
                } else { //остальные парсим
                    lineColor = getColorFromTdStyle(cells.select("td[style]").attr("style"));
                }
                Line currentLine = new Line(lineNumber, lineName, lineColor);


                stationIndexForParsing.addLine(currentLine);
            }
        }
    }

    private void parseStationFromWiki(StationIndex stationIndexForParsing) throws IOException {
        Elements tableLine = getTableLineFromWiki();

        for (Element element : tableLine) { //Перебираем все строки таблиц, содержащих инфо о станциях
            Elements cells = element.select("td"); //Получаем элементы, содержащие ячейки каждой линии
            //Если ячеек 7 - это монорельс или Московское центральное кольцо
            //Если ячеек 8 - это обычное метро

            if (cells.size() == 7 || cells.size() == 8) { //Если в строке 7 или 8 ячеек то парсим инфу оттуда
                String lineNumber = cells.select("span.sortkey").first().text(); //Получаем номер линии
                Line currentLine = stationIndexForParsing.getLine(lineNumber);
                System.out.println("Линия " + currentLine.getName() + " получена");
                String stationName = cells.get(1).select("a").text().replaceAll("[0-9]", "");
                Station currentStation = new Station(stationName, currentLine);
                System.out.println("Станция " + currentStation.getName() + " на линии " + currentLine.getName() + " создана");
                stationIndexForParsing.addStation(currentStation);
                System.out.println("Станция добавлено в метро");
                currentLine.addStation(currentStation);
                System.out.println("Станция добавлена на линию");
            }
        }
    }

//    private void parseConnectionFromWiki(StationIndex stationIndex) throws IOException { //Парсим переходы
//        Elements tableLine = getTableLineFromWiki();
//        for (Element element : tableLine) { //Парсим переходы в этом цикле
//            Elements cells = element.select("td"); //Получаем ячейки в каждом элементе.
//            if (cells.size() == 7 || cells.size() == 8) { //Если в строке 7 или 8 ячеек то парсим инфу оттуда
//                List<Station> connection = new ArrayList<>(); //Перехды
//                //Информация  переходе находится в 3-ей слева ячейки строки
//                String stationName = cells.get(1).select("a").text().replaceAll("[0-9]", "");
//                String stationLine = cells.select("span.sortkey").first().text(); //Номер линии
//                System.out.println("stationName = " + stationName);
//                System.out.println("stationLine = " + stationLine);
//                Station currentStation = metro.getStation(stationName, stationLine);
//                System.out.println(currentStation);
//                //Если переходов нет, то <td data-sort-value="Infinity">
//                Boolean hasConnections = !cells.get(3).attr("data-sort-value").equals("Infinity");
//                if (!hasConnections) continue;
//                Elements confectionsUrlElement = cells.get(3).select("a");
//                connection.add(currentStation);
//                for (Element linkToStationOnWiki : confectionsUrlElement) {
//                    String connectedStationName = URLDecoder.decode(linkToStationOnWiki.attr("href"), "UTF-8");
//                    connectedStationName = delAllExcessCharsOfStationName(connectedStationName);
//                    connection.add(stationIndex.getStation(connectedStationName));
//                }
////                if (connection != null)
////                    stationIndex.addConnection(connection);
//            }
//        }
//    }
}
