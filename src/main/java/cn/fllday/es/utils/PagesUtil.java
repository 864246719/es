package cn.fllday.es.utils;

import java.util.List;

/*
 *  分页显示数据工具类
 */
public class PagesUtil<T> {

    private int pageIndex; // 当前页数
    private int count;     // 总记录数
    private int size;      // 页面显示行数
    private int totalPages; // 总页数
    private List<T> list;      // 当前页面显示的数据集合

    public PagesUtil() {
    }


    public void setPageIndex(int pageIndex) {
        // 验证最小访问页面不能小于1
        if(pageIndex < 1)
            this.pageIndex = 1;
        // 验证最大访问页面不能超过最大页码数
        else if (totalPages > 0 && pageIndex > totalPages)
            this.pageIndex = totalPages;
        else
            this.pageIndex = pageIndex;
    }


    public void setTotalPages(int count, int size) {
        this.count = count;
        this.size = size;
        if(count > 0) {
            this.totalPages = (count - 1) / size + 1;
        } else {
            this.totalPages = 0;
        }
    }


    public void setList(List<T> list) {
        this.list = list;
    }


    public int getCount() {
        return count;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public List<T> getList() {
        return list;
    }
}
