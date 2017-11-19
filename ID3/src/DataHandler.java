import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 将一个二维数组当作数据库中的 table 看待，第 0 行存放的是所有列名称，
 * 此类提供查询、查询(distinct)、和去除一些行、仅保留一些行的操作
 */
class DataHandler {

    /**
     * 从 table 中获取某一列的所有数据
     *
     * @param table     二维数组，当表格看待
     * @param columnName 列名，即二维数组的第 0 行某一列的名称
     * @return List 存放指定列的所有数据，相同数据只出现一个
     */
    static List<String> queryDistinct(String[][] table, String columnName) {
        return queryDistinct(table, columnName, null, null);
    }

    /**
     * 从 table 中获取某一列的符合条件的所有数据，使用 whereClause 指定符合条件的行
     *
     * @param table      二维数组，当表格看待
     * @param columnName  列名，即二维数组的第 0 行某一列的名称
     * @param whereClause 仿造 sql 的 where 语句写法，使用 ? 当做占位符，如 “name=?”
     * @param conditions  提供 columnName 中占位符的值，必须和 ? 一一对应
     * @return List 存放指定列的所有数据，相同数据只出现一个
     */
    static List<String> queryDistinct(String[][] table, String columnName, String whereClause, String[] conditions) {

        Map<String, String> map = new HashMap<>();

        if (whereClause != null && conditions != null) {
            whereClause = whereClause.replaceAll("and", " ");

            for (String condition : conditions) {
                whereClause = whereClause.replaceFirst("\\?", " " + condition + ";");
            }
            if (whereClause.contains("?")) {
                throw new IllegalArgumentException("占位符数目不匹配！");
            }

            String[] attrPairs = whereClause.split(";");
            for (String attrPair : attrPairs) {
                String[] attrPairArray = attrPair.split("=");
                map.put(attrPairArray[0].trim(), attrPairArray[1].trim());
            }
        }


        int index = columnOf(columnName, table);
        List<String> distinctValues = new ArrayList<>();
        for (int i = 1; i < table.length; i++) {
            boolean targetRow = true;
            for (Map.Entry<String, String> next : map.entrySet()) {
                String s = next.getKey();
                String s2 = next.getValue();

                if (!table[i][columnOf(s, table)].equals(s2)) {
                    targetRow = false;
                    break;
                }
            }

            if (targetRow && !distinctValues.contains(table[i][index]))
                distinctValues.add(table[i][index]);
        }

        return distinctValues;
    }

    /**
     * 从 table 中获取某一列的符合条件的所有数据，使用 whereClause 指定符合条件的行
     *
     * @param table      二维数组，当表格看待
     * @param columnName  列名，即二维数组的第 0 行某一列的名称
     * @param whereClause 仿造 sql 的 where 语句写法，使用 ? 当做占位符，如 “name=?”
     * @param conditions  提供 columnName 中占位符的值，必须和 ? 一一对应
     * @return List 存放指定列的所有数据，相同数据也会出现
     */
    static List<String> query(String[][] table, String columnName, String whereClause, String[] conditions) {

        Map<String, String> map = new HashMap<>();

        if (whereClause != null && conditions != null) {
            whereClause = whereClause.replaceAll("and", " ");

            for (String condition : conditions) {
                whereClause = whereClause.replaceFirst("\\?", " " + condition + ";");
            }
            if (whereClause.contains("?")) {
                throw new IllegalArgumentException("占位符数目不匹配！");
            }

            String[] attrPairs = whereClause.split(";");
            for (String attrPair : attrPairs) {
                String[] attrPairArray = attrPair.split("=");
                map.put(attrPairArray[0].trim(), attrPairArray[1].trim());
            }
        }


        int index = columnOf(columnName, table);
        List<String> values = new ArrayList<>();
        for (int i = 1; i < table.length; i++) {
            boolean targetRow = true;
            for (Map.Entry<String, String> next : map.entrySet()) {
                String s = next.getKey();
                String s2 = next.getValue();

                if (!table[i][columnOf(s, table)].equals(s2)) {
                    targetRow = false;
                    break;
                }
            }

            if (targetRow)
                values.add(table[i][index]);
        }

        return values;
    }

    /**
     * 舍弃 table 中符合条件的一些行
     *
     * @param table     二维数组，当表格看待
     * @param columnName 列名
     * @param value      列名的值
     * @return columnName!=value 的所有行组成的二维数组
     */
    static String[][] withoutRows(String[][] table, String columnName, String value) {
        int col = columnOf(columnName, table);
        int length = table.length;
        for (int i = 1; i < table.length; i++) {
            if (table[i][col].equals(value)) {
                table[i][0] = "waitForDelete";
                length--;
            }
        }

        String[][] result = new String[length][table[0].length];
        int j = 0;
        for (int i = 0; i < table.length; i++) {
            if (!table[i][0].equals("waitForDelete"))
                result[j++] = table[i];
        }
        return result;

    }

    /**
     * 仅保留 table 中符合条件的一些行
     *
     * @param table     二维数组，当表格看待
     * @param columnName 列名
     * @param value      列名的值
     * @return columnName==value 的所有行组成的二维数组
     */
    static String[][] leftOnlyRows(String[][] table, String columnName, String value) {
        int col = columnOf(columnName, table);
        int length = table.length;
        for (int i = 1; i < table.length; i++) {
            if (!table[i][col].equals(value)) {
                table[i][0] = "waitForDelete";
                length--;
            }
        }

        String[][] result = new String[length][table[0].length];
        int j = 0;
        for (int i = 0; i < table.length; i++) {
            if (!table[i][0].equals("waitForDelete"))
                result[j++] = table[i];
        }
        return result;

    }

    /**
     * 给定一个列名，返回它在 table 中的列索引
     *
     * @param columnName 列名
     * @param table     二维数组，当表格看待
     * @return columnName 在 table 中的列索引
     */
    private static int columnOf(String columnName, String[][] table) {
        int index = 0;
        for (; index < table[0].length; index++) {
            if (table[0][index].equals(columnName)) {
                break;
            }
        }
        return index;
    }


}
