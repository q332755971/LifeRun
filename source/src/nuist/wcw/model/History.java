package nuist.wcw.model;

public class History {
	private long date; // 日期
	private int duration; // 跑步持续时间
	private int distance; // 跑步路程
	private int heat; // 消耗热量

	/**
	 * @param date
	 *            日期
	 * @param duration
	 *            持续时间
	 * @param distance
	 *            运动距离
	 * @param heat
	 *            消耗热量
	 */
	public History(long date, int duration, int distance, int heat) {
		super();
		this.date = date;
		this.duration = duration;
		this.distance = distance;
		this.heat = heat;
	}

	/**
	 * @return date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            要设置的 duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            要设置的 distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * @return heat
	 */
	public int getHeat() {
		return heat;
	}

	/**
	 * @param heat
	 *            要设置的 heat
	 */
	public void setHeat(int heat) {
		this.heat = heat;
	}

}
