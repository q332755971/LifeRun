package nuist.wcw.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * XML文件解析，将从WebService获取的XML数据进行解析，并返回 文件全部通过静态方法实现。
 * 
 * @author 晨崴
 * 
 */
public class XMLAnalyzing {

	/**
	 * 从XML数据中解析出城市名称并返回
	 * 
	 * @param result
	 *            XML文件内容
	 * @return 城市名
	 */
	public static String getCity(String result) {
		String city = null;
		if (result.contains("省")) {
			city = result.substring(result.indexOf("省") + 1,
					result.indexOf("市"));
		} else if (result.contains("上海")) {
			city = "上海";
		} else if (result.contains("北京")) {
			city = "北京";
		} else if (result.contains("重庆")) {
			city = "重庆";
		} else if (result.contains("天津")) {
			city = "天津";
		} else if (result.contains("西藏")) {
			if (result.contains("市")) {
				city = "拉萨";
			} else if (result.contains("地区")) {
				city = result.substring(result.indexOf("藏") + 1,
						result.indexOf("地区"));
			}
		} else if (result.contains("内蒙古")) {
			if (result.contains("锡林")) {
				city = "锡林浩特";
			} else if (result.contains("呼伦贝尔") || result.contains("海拉尔")) {
				city = "海拉尔";
			} else {
				city = result.substring(result.indexOf("古") + 1,
						result.indexOf("市"));
			}
		} else if (result.contains("新疆")) {
			if (result.contains("市")) {
				city = result.substring(result.indexOf("疆") + 1,
						result.indexOf("市"));
			} else if (result.contains("地区")) {
				city = result.substring(result.indexOf("疆") + 1,
						result.indexOf("地区"));
			}
		} else if (result.contains("宁夏") || result.contains("广西")) {
			city = result.substring(result.indexOf("区") + 1,
					result.indexOf("市"));
		} else if (result.contains("香港")) {
			city = "香港";
		} else if (result.contains("澳门")) {
			city = "澳门";
		}
		return city;
	}

	/**
	 * 解析网页数据获得温天气
	 * 
	 * @param result
	 *            XML文件内容
	 * @return 天气情况
	 */
	public static String getWeather(String result) {
		Document doc = Jsoup.parse(result);
		Elements eles = doc.getElementsByTag("string");
		String wea = eles.get(6).text();
		String weather = wea.substring(wea.indexOf(" ") + 1);
		return weather;
	}

	/**
	 * 解析网页数据获得温度
	 * 
	 * @param result
	 *            XML文件内容
	 * @return 实时温度
	 */
	public static String getTemperature(String result) {
		Document doc = Jsoup.parse(result);
		Elements eles = doc.getElementsByTag("string");
		String temp = eles.get(10).text();
		String temperature = temp.substring(temp.indexOf("气温：") + 3,
				temp.indexOf("；风向"));
		return temperature;
	}
}
