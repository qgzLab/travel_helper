#encoding utf-8
from django.db import models
class User(models.Model):#用户
    Username = models.CharField(max_length = 50)
    Password = models.CharField(max_length = 30)
    Email = models.CharField(max_length = 30)
    Phone = models.CharField(max_length = 15)
    Sex = models.CharField(max_length = 5)
    Birth_year = models.IntegerField()
    Birth_month = models.IntegerField()
    Birth_day = models.IntegerField()
    Qq = models.CharField(max_length = 15)
    Live_province = models.CharField(max_length = 30)
    Live_city = models.CharField(max_length = 30)
    Img = models.FileField(upload_to = './load/')
class Team(models.Model):#团队
    Team_name = models.CharField(max_length = 30)
    Team_province = models.CharField(max_length = 100)
    Team_city = models.CharField(max_length = 100)
    Leader = models.ForeignKey(User,related_name = "my_team")
    User0 = models.ManyToManyField(User,related_name = "team0")#已审核队员
    User1 = models.ManyToManyField(User,related_name = "team1")#邀请未审核队员  
    User2 = models.ManyToManyField(User,related_name = "team2")#申请未审核队员

class Diary(models.Model):#日志 kind = 0
    Dia_province = models.CharField(max_length = 30)
    Dia_city = models.CharField(max_length = 30)
    Dia_place = models.CharField(max_length = 30)
    Dia_title = models.CharField(max_length = 30)
    Dia_context = models.CharField(max_length = 1000)
    Dia_author = models.ForeignKey(User,related_name='diary')
    Dia_time = models.CharField(max_length = 40)
    Dia_zan = models.IntegerField()#点赞数
    Img = models.FileField(upload_to = './load/')
class Guide(models.Model):#攻略，kind =1
    Gui_province = models.CharField(max_length = 30)
    Gui_city = models.CharField(max_length = 30)
    Gui_place = models.CharField(max_length = 30)
    Gui_title = models.CharField(max_length = 40)
    Gui_context = models.CharField(max_length = 1000)
    Gui_author = models.ForeignKey(User,related_name = 'guide')
    Gui_time = models.CharField(max_length = 40)
    Gui_flag = models.BooleanField()#是否为官方攻略
    Gui_zan = models.IntegerField()#点赞数 
    Img = models.FileField(upload_to = './load/')
class Ans_guide(models.Model):
    Ans_context = models.CharField(max_length = 1000)
    Ans_time = models.CharField(max_length = 30)
    Ans_author = models.ForeignKey(User,related_name = 'ans_guide')
    guide = models.ForeignKey(Guide,related_name = 'a_guide')
class Ans_diary(models.Model):
    Ans_context = models.CharField(max_length = 1000)
    Ans_time = models.CharField(max_length = 30)
    Ans_author = models.ForeignKey(User,related_name = 'ans_diary')
    diary = models.ForeignKey(Diary,related_name = 'a_diary')  
class Aim(models.Model):#意向
    Province = models.CharField(max_length = 30)
    City = models.CharField(max_length = 30)
    Start_t = models.CharField(max_length = 30)
    End_t = models.CharField(max_length = 30)
    Price = models.CharField(max_length = 5)
    User = models.ForeignKey(User,related_name='aim')
class Admin(models.Model):#管理员
    Email = models.CharField(max_length = 30)
    password = models.CharField(max_length = 40)
    
    
    
    

