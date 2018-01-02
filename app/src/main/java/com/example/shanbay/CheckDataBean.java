package com.example.shanbay;

import java.util.List;

/**
 * Created by 上官轩明 on 2017/12/12.
 */

public class CheckDataBean {

    /**
     * msg : SUCCESS
     * status_code : 0
     * data : {"tasks":[{"status":0,"is_in_plan":0,"finished":true,"meta":{"num_left":0,"url":"/bdc/review/","num_today":100,"used_time":1,"correctness":0},"name":"bdc","daily_init_finished":true,"task_name_en":"Vocabulary","tip":"完成","task_name_cn":"单词","redirect_url":"/review/","msg":"你今天学习了 100 个单词，用时 1 分钟"},{"status":0,"name":"listen","plan_url":"/listen/plan/","daily_init_finished":true,"task_name_en":"Listen","task_name_cn":"听力","is_in_plan":0,"finished":false,"meta":{"num_left":5,"url":"/listen/","num_today":5,"used_time":0},"redirect_url":"/listen/","msg":"你还没开始今天的听力任务"},{"status":0,"finished":false,"meta":{"num_left":2,"url":"/read/home/","num_today":2,"used_time":0,"num_articles":0},"name":"read","plan_url":"/read/plan/","msg":"你今天需要完成 2 篇阅读，还剩下 2 篇","daily_init_finished":true,"task_name_en":"Reading","task_name_cn":"阅读","is_in_plan":0},{"status":0,"finished":false,"meta":{"num_left":10,"num_sentences":10,"url":"/sentence/review/","num_new":0,"num_finished":0,"used_time":0,"num_today":10},"name":"sentence","plan_url":"/sentence/plan/","msg":"你尚未开始今天的炼句任务","daily_init_finished":false,"task_name_en":"Sentence","task_name_cn":"炼句","is_in_plan":0},{"finished":false,"meta":{"num_left":1,"url":"","num_today":1,"used_time":0},"name":"speak","redirect_url":"","msg":"You haven't started today's speak task yet","task_name_en":"Speak","task_name_cn":"口语","is_in_plan":0},{"finished":false,"meta":{"num_left":3,"url":"","num_today":3,"used_time":0},"name":"elevator","redirect_url":"","msg":"You haven't started today's elevator task yet","task_name_en":"Elevator","task_name_cn":"阶梯训练","is_in_plan":0}],"checked":false,"share_type":"image","num_checkin_days":2,"finished":true,"need_captcha":true,"user_note":""}
     */

    private String msg;
    private int status_code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * tasks : [{"status":0,"is_in_plan":0,"finished":true,"meta":{"num_left":0,"url":"/bdc/review/","num_today":100,"used_time":1,"correctness":0},"name":"bdc","daily_init_finished":true,"task_name_en":"Vocabulary","tip":"完成","task_name_cn":"单词","redirect_url":"/review/","msg":"你今天学习了 100 个单词，用时 1 分钟"},{"status":0,"name":"listen","plan_url":"/listen/plan/","daily_init_finished":true,"task_name_en":"Listen","task_name_cn":"听力","is_in_plan":0,"finished":false,"meta":{"num_left":5,"url":"/listen/","num_today":5,"used_time":0},"redirect_url":"/listen/","msg":"你还没开始今天的听力任务"},{"status":0,"finished":false,"meta":{"num_left":2,"url":"/read/home/","num_today":2,"used_time":0,"num_articles":0},"name":"read","plan_url":"/read/plan/","msg":"你今天需要完成 2 篇阅读，还剩下 2 篇","daily_init_finished":true,"task_name_en":"Reading","task_name_cn":"阅读","is_in_plan":0},{"status":0,"finished":false,"meta":{"num_left":10,"num_sentences":10,"url":"/sentence/review/","num_new":0,"num_finished":0,"used_time":0,"num_today":10},"name":"sentence","plan_url":"/sentence/plan/","msg":"你尚未开始今天的炼句任务","daily_init_finished":false,"task_name_en":"Sentence","task_name_cn":"炼句","is_in_plan":0},{"finished":false,"meta":{"num_left":1,"url":"","num_today":1,"used_time":0},"name":"speak","redirect_url":"","msg":"You haven't started today's speak task yet","task_name_en":"Speak","task_name_cn":"口语","is_in_plan":0},{"finished":false,"meta":{"num_left":3,"url":"","num_today":3,"used_time":0},"name":"elevator","redirect_url":"","msg":"You haven't started today's elevator task yet","task_name_en":"Elevator","task_name_cn":"阶梯训练","is_in_plan":0}]
         * checked : false
         * share_type : image
         * num_checkin_days : 2
         * finished : true
         * need_captcha : true
         * user_note :
         */

        private boolean checked;
        private String share_type;
        private int num_checkin_days;
        private boolean finished;
        private boolean need_captcha;
        private String user_note;
        private List<TasksBean> tasks;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getShare_type() {
            return share_type;
        }

        public void setShare_type(String share_type) {
            this.share_type = share_type;
        }

        public int getNum_checkin_days() {
            return num_checkin_days;
        }

        public void setNum_checkin_days(int num_checkin_days) {
            this.num_checkin_days = num_checkin_days;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public boolean isNeed_captcha() {
            return need_captcha;
        }

        public void setNeed_captcha(boolean need_captcha) {
            this.need_captcha = need_captcha;
        }

        public String getUser_note() {
            return user_note;
        }

        public void setUser_note(String user_note) {
            this.user_note = user_note;
        }

        public List<TasksBean> getTasks() {
            return tasks;
        }

        public void setTasks(List<TasksBean> tasks) {
            this.tasks = tasks;
        }

        public static class TasksBean {
            /**
             * status : 0
             * is_in_plan : 0
             * finished : true
             * meta : {"num_left":0,"url":"/bdc/review/","num_today":100,"used_time":1,"correctness":0}
             * name : bdc
             * daily_init_finished : true
             * task_name_en : Vocabulary
             * tip : 完成
             * task_name_cn : 单词
             * redirect_url : /review/
             * msg : 你今天学习了 100 个单词，用时 1 分钟
             * plan_url : /listen/plan/
             */

            private int status;
            private int is_in_plan;
            private boolean finished;
            private MetaBean meta;
            private String name;
            private boolean daily_init_finished;
            private String task_name_en;
            private String tip;
            private String task_name_cn;
            private String redirect_url;
            private String msg;
            private String plan_url;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getIs_in_plan() {
                return is_in_plan;
            }

            public void setIs_in_plan(int is_in_plan) {
                this.is_in_plan = is_in_plan;
            }

            public boolean isFinished() {
                return finished;
            }

            public void setFinished(boolean finished) {
                this.finished = finished;
            }

            public MetaBean getMeta() {
                return meta;
            }

            public void setMeta(MetaBean meta) {
                this.meta = meta;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isDaily_init_finished() {
                return daily_init_finished;
            }

            public void setDaily_init_finished(boolean daily_init_finished) {
                this.daily_init_finished = daily_init_finished;
            }

            public String getTask_name_en() {
                return task_name_en;
            }

            public void setTask_name_en(String task_name_en) {
                this.task_name_en = task_name_en;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }

            public String getTask_name_cn() {
                return task_name_cn;
            }

            public void setTask_name_cn(String task_name_cn) {
                this.task_name_cn = task_name_cn;
            }

            public String getRedirect_url() {
                return redirect_url;
            }

            public void setRedirect_url(String redirect_url) {
                this.redirect_url = redirect_url;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getPlan_url() {
                return plan_url;
            }

            public void setPlan_url(String plan_url) {
                this.plan_url = plan_url;
            }

            public static class MetaBean {
                /**
                 * num_left : 0
                 * url : /bdc/review/
                 * num_today : 100
                 * used_time : 1.0
                 * correctness : 0
                 */

                private int num_left;
                private String url;
                private int num_today;
                private double used_time;
                private int correctness;

                public int getNum_left() {
                    return num_left;
                }

                public void setNum_left(int num_left) {
                    this.num_left = num_left;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getNum_today() {
                    return num_today;
                }

                public void setNum_today(int num_today) {
                    this.num_today = num_today;
                }

                public double getUsed_time() {
                    return used_time;
                }

                public void setUsed_time(double used_time) {
                    this.used_time = used_time;
                }

                public int getCorrectness() {
                    return correctness;
                }

                public void setCorrectness(int correctness) {
                    this.correctness = correctness;
                }
            }
        }
    }
}
