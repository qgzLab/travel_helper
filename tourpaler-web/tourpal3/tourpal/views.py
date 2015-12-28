# -*- coding: utf-8 -*-
from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_protect
from django.shortcuts import render_to_response,RequestContext
from tourpal.models import *
from django.contrib.sessions.models import Session
from tourpal.forms import *
import urllib2
import json
from django.utils.http import urlquote
def home(request):#主页
    logined  = False
    user = []
    if 'memberid' in request.session:
        logined = True
        user = User.objects.get(id = request.session['memberid'])   
    return render_to_response('home.html',{'User':user,'logined':logined})
def register(request):#注册
    if 'uname' in request.POST:#detail the form submited
        name = request.POST['uname']
        password = request.POST['upassword']
        email = request.POST['uemail']
        phone = request.POST['uphone']
        sex = request.POST['usex']
        qq = request.POST['qq']
        year = int(request.POST['year'])
        month = int(request.POST['month'])
        day = int(request.POST['day'])
        province = request.POST['region1']
        city = request.POST['region2']
        User.objects.create(Username = name, Password = password, Email = email,
                            Phone = phone, Sex = sex, Birth_year = year, Birth_month = month,
                            Birth_day = day,Qq = qq, Live_province =province, Live_city = city,Img = 'load/defailt.jpg')
        #dont login,request the user to login
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
    else:# when the form is empty
        return render_to_response('register.html',{'userid':'0'},context_instance=RequestContext(request))
def login(request):#登陆
    if 'email' in request.POST:
        email = request.POST['email']
        if email:
            try:
                user = User.objects.get(Email = email)
                if user.Password == (request.POST['password']):
                    request.session['memberid'] = user.id
                    
                    # login succeed
                    return render_to_response('home.html',{'logined':'1','User':user},context_instance=RequestContext(request))
                else:
                    # the password is wrong
                    return render_to_response('login.html',{'error':'1'},context_instance=RequestContext(request))
            except:
                return render_to_response('register.html',{'userid':'0'},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',context_instance=RequestContext(request))
    else:
        return render_to_response('login.html',context_instance=RequestContext(request))
def loginout(request):#登出
    if 'memberid' in request.session:
        del request.session['memberid']        
        
        return render_to_response('home.html',context_instance = RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def information(request):#个人信息展示
    if 'memberid' in request.session:
        if request.session['memberid']:
            id = request.session['memberid']
            user = User.objects.get(id = id)
            return render_to_response('information.html',{'User':user,'logined':1},context_instance = RequestContext(request))
        else:
            # request to login
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
    #request to login
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def ch_information(request):#修改个人信息
        if 'memberid' in request.session:#check login
            if 'uname' in request.POST:#resive the change form
                id = request.session['memberid']                
                name = request.POST['uname']                
                phone = request.POST['uphone']
                sex = request.POST['usex']
                qq = request.POST['qq']
                year = int(request.POST['year'])
                month = int(request.POST['month'])
                day = int(request.POST['day'])
                province = request.POST['region1']
                city = request.POST['region2']
                User.objects.filter(id = id).update(Username = name,Phone = phone, Sex = sex, Birth_year = year, Birth_month = month,
                            Birth_day = day,Qq = qq, Live_province =province, Live_city = city)
                return render_to_response('home.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
            else:#return thr unchangeed form
                id = request.session['memberid']
                user = User.objects.get(id = id)
                return render_to_response('change_inf.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
        else:#request to login
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def ch_pass(request):#修改密码
    if 'memberid' in request.session:
        if 'old_password' in request.POST:
            old = request.POST['old_password']
            new = request.POST['new_password_1']
            id = request.session['memberid'] 
            if old == User.objects.get(id = id).Password:
                User.objects.filter(id = id).update(Password = new)
                del request.session['memberid']
                return render_to_response('home.html',{'un_login':'1','User':User.objects.get(id = id)},context_instance= RequestContext(request))
            else :
                return render_to_response('change_pass.html',{'oldwrong':'1','logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
            
        else:
            return render_to_response('change_pass.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
    return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def aim(request):#意向主界面
        if 'memberid' in request.session:#logined
            id = request.session['memberid']
            user = User.objects.get(id = id)
            aims = user.aim.all()
            if aims:#not empty
                dic = {'1':'0-200','2':'200-500','3':'500-1000','4':'1000-1500','5':'1500-2500','6':'2500-4000','7':'4000-'}
                return render_to_response('aim.html',{'aims':aims,'dic':dic,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
            else:#empty         
                if 'province' in request.POST:# submit form
                    province = request.POST['province']
                    city = request.POST['city']
                    year = request.POST['year']
                    month =request.POST['month']
                    day = request.POST['day']
                    year1 = request.POST['year1']
                    month1 =request.POST['month1']
                    day1 = request.POST['day1']
                    start_t = ''+year+month+day
                    end_t = ''+year1+month1+day1
                    price = request.POST['price']
                    Aim.objects.create(Province = province, City = city, Start_t = start_t, End_t = end_t, Price = price,
                                        User_id = request.session['memberid'])
                    aims_add =user.aim.all()
                    return render_to_response('aim.html',{'aims':aims_add,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
                else:#return form
                    return render_to_response('aim_form.html',{'aim_empty':'1','logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))      
def aim_add(request):#添加意向
        if 'memberid' in request.session:
            if 'province' in request.POST:# submit form
                    province = request.POST['province']
                    city = request.POST['city']
                    year = request.POST['year']
                    month =request.POST['month']
                    day = request.POST['day']
                    year1 = request.POST['year1']
                    month1 =request.POST['month1']
                    day1 = request.POST['day1']
                    start_t = ''+year+month+day
                    end_t = ''+year1+month1+day1
                    price = request.POST['price']
                    Aim.objects.create(Province = province, City = city, Start_t = start_t, End_t = end_t, Price = price,
                                        User_id = request.session['memberid'])
                    aims_add =User.objects.get(id = request.session['memberid'] ).aim.all()
                    return render_to_response('aim.html',{'aims':aims_add,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
            else:#return form
                    return render_to_response('aim_form.html',{'aim_empty':0,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
            
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def aim_change(request):#修改意向
        if 'memberid' in request.session:
            aim_id = request.GET['aim_id']
            if 'province' in request.POST:
                province = request.POST['province']
                city = request.POST['city']
                year = request.POST['year']
                month =request.POST['month']
                day = request.POST['day']
                year1 = request.POST['year1']
                month1 =request.POST['month1']
                day1 = request.POST['day1']
                start_t = ''+year+month+day
                end_t = ''+year1+month1+day1
                price = request.POST['price']
                Aim.objects.filter(id = aim_id).update(Province = province, City = city, Start_t = start_t, End_t = end_t, Price = price,
                                    User_id = request.session['memberid'])
                aims =User.objects.get(id = request.session['memberid'] ).aim.all()
                return render_to_response('aim.html',{'aims':aims,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
            else:
                aim = Aim.objects.get(id = aim_id)
                date1='' + aim.Start_t
                date2=''+ aim.End_t
                date3 = {}
                date4 = {}
                date3['0'] = date1[0:4]
                date3['1'] = date1[4:6]
                date3['2'] = date1[6:8]
                date4['0'] = date2[0:4]
                date4['1'] = date2[4:6]
                date4['2'] = date2[6:8]
                dic = {'1':'0-200','2':'200-500','3':'500-1000','4':'1000-1500','5':'1500-2500','6':'2500-4000','7':'4000-'}
                return render_to_response('aim_change.html',{'aim':aim,'logined':'1','dic':dic,'date3':date3,'date4':date4,'User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
                 
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))         
def aim_delete(request):#删除意向
        if 'memberid' in request.session:
            aim_id = request.GET['aim_id']
            Aim.objects.get(id = aim_id).delete()
            aims = User.objects.get(id = request.session['memberid'] ).aim.all()
            return render_to_response('aim.html',{'aims':aims,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def search(request):#地图查询
        if 'address' in request.POST:
            address = request.POST['address']
            address = urlquote(address)
            url = 'http://api.map.baidu.com/geocoder?address='+ address +'&output=json&key=37492c0ee6f924cb5e934fa08c6b1676'
            response = urllib2.urlopen(url)         #调用urllib2向服务器发送get请求
            ret = json.loads(response.read())
            if 'location' in ret['result']:
                lng = ret['result']['location']['lng']
                lat = ret['result']['location']['lat']
                return render_to_response('map.html',{'lng':lng,'lat':lat,'flag':0,'addre':request.POST['address']},context_instance = RequestContext(request))
            else:
                return render_to_response('map.html',{'flag':1},context_instance = RequestContext(request))
        else:
            return render_to_response('search_form.html',context_instance = RequestContext(request))
def diary(request):#撰写日志
        if 'memberid' in request.session:
            return render_to_response('diary_form.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))          
def mydiary(request):#日志主界面
        if 'memberid' in request.session:
            id = request.session['memberid']
            diarys = User.objects.get(id = id).diary.all()
            if diarys:
                return render_to_response('diary.html',{'diarys':diarys,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
            else:
                return render_to_response('diary_form.html',{'empty':'1','logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def dia_gui_add(request):#添加日志&攻略
        if 'memberid' in request.session:
            id = request.session['memberid']
            if 'title' in request.POST:               
                province = request.POST['region1']
                city = request.POST['region2']
                place = request.POST['dest']
                title = request.POST['title']
                context = request.POST['text']
                if 'pic' in request.FILES:               
                    pic = request.FILES['pic']
                else:
                    pic = None
                check_box_list = request.REQUEST.getlist('choose')
                for i in check_box_list:
                    if i == '1':
                        Diary.objects.create(Dia_title = title,Dia_context = context, Dia_province = province, 
                                         Dia_city = city,Dia_place = place,Dia_time = '2015/12/25', Dia_author_id = id, Dia_zan = 0, Img = pic)
                    elif i == '2':
                        Guide.objects.create(Gui_title = title, Gui_context = context, Gui_province = province,
                                            Gui_city = city, Gui_place = place, Gui_time = '2015/12/25',Gui_author_id = id, Gui_zan = 0, Gui_flag = False, Img = pic)
                return render_to_response('home.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
             
            else:
                return render_to_response('diary_form.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def diary_inf(request,diaryid):#日志的详细信息
        if 'memberid' in request.session:
            id = int(diaryid)
            diary = Diary.objects.get(id = id)
            answers = diary.a_diary.all()
            return render_to_response('diary_inf.html',{'diary':diary,'answers':answers,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def diary_delete(request):#删除日志
        if 'memberid' in request.session:
            diary_id = request.GET['diary_id']
            Diary.objects.get(id = diary_id).delete()
            id = request.session['memberid']
            diarys = User.objects.get(id = id).diary.all()
            if diarys:
                return render_to_response('diary.html',{'diarys':diarys,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
            else:
                return render_to_response('diary_form.html',{'empty':'1','logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def diary_change(request):#修改日志
        if 'memberid' in request.session:
            diary_id = request.GET['diary_id']
            diary = Diary.objects.get(id = diary_id)
            answers = diary.a_diary.all()
            if 'title' in request.POST:
                province = request.POST['region1']
                city = request.POST['region2']
                title = request.POST['title']
                context = request.POST['text']
                diary.Dia_province = province
                diary.Dia_city = city
                diary.Dia_title = title
                diary.Dia_context = context
                diary.save()
                diary = Diary.objects.get(id = diary_id)
                answers = diary.a_diary.all()
                return render_to_response('diary_inf.html',{'diary':diary,'answers':answers,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))

            else:
                return render_to_response('diary_change.html',{'diary':diary,'answers':answers,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance= RequestContext(request))
        else:
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def team(request):#团队主界面
    if 'memberid' in request.session:
        id = request.session['memberid']
        user = User.objects.get(id = id)
        teams = user.my_team.all()
        team0 = user.team0.all()
        team1 = user.team1.all()
        team2 = user.team2.all()
        return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
        'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def team_add(request,aim_id):#增加团队
    if 'memberid' in request.session:#logined
        if 'name' in request.POST:#submit the information of the team
            team_name = request.POST['name']
            province = request.POST['province']
            city = request.POST['city']
            dest = request.POST['dest']
            team = Team.objects.create(Team_name  = team_name, Team_province = province,Team_city = city,Leader_id = request.session['memberid'])
            #创建团队
            aim = Aim.objects.get(id  = aim_id)
            start = aim.Start_t
            #推荐队友
            aims = Aim.objects.filter(City = aim.City ).filter(Price = aim.Price).filter(Start_t__lte=start).filter(End_t__gte=start).exclude(id = aim_id)
            users = []
            for aim1 in aims:
                user = aim1.User
                users.append(user)
            return render_to_response('team_advice.html',{'users':users,'team_name':team_name,'team_id':team.id,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))    
        else:#return the team_form
            return render_to_response('team_form.html',{'aim_id':aim_id,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def team_del(request,team_id):#删除团队
        id = team_id
        user = User.objects.get(id = request.session['memberid'])
        team = Team.objects.get(id = id)
        if user.id == team.Leader_id:
            team.delete()
        else:
            team.User0.remove(user)
        id = request.session['memberid']
        user = User.objects.get(id = id)
        teams = user.my_team.all()
        team0 = user.team0.all()
        team1 = user.team1.all()
        team2 = user.team2.all()
        return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
        'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request))
def teamMemAdd(request,team_id):#邀请队友
    if 'memberid' in request.session:#logined         
        team = Team.objects.get(id = team_id)
        check_box_list = request.REQUEST.getlist('members')
        if check_box_list:
            for id in check_box_list:
                user = User.objects.get(id = int(id))
                team.User1.add(user)
        id = request.session['memberid']
        user = User.objects.get(id = id)
        teams = user.my_team.all()
        team0 = user.team0.all()
        team1 = user.team1.all()
        team2 = user.team2.all()
        return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
        'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request))           
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def teamAdd(request):#申请加入团队
    if 'memberid' in request.session:#logined 
            id = request.session['memberid']
            user = User.objects.get(id = id)
            if 'teamlist' in request.POST:
                check_box_list = request.REQUEST.getlist('teamlist')
                if check_box_list:
                    for team_id in check_box_list:
                        Team.objects.get(id = team_id).User2.add(user)
            teams = user.my_team.all()
            team0 = user.team0.all()
            team1 = user.team1.all()
            team2 = user.team2.all()
            return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
            'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request)) 
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
def teamfind(request):#查找团队
    if 'memberid' in request.session:#logined 
        if request.method == "POST":        
            id = request.session['memberid']
            user = User.objects.get(id = id)
            name = request.POST['name']
            teams = Team.objects.filter(Team_name = name)
            return render_to_response('teamfind_form.html',{'teams':teams},context_instance=RequestContext(request)) 
        else:
            return render_to_response('teamfind_form.html',context_instance=RequestContext(request)) 
            

    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def guide(request):#攻略主界面
    if 'memberid' in request.session:#logined   
        if 'dest' in request.POST:
            province = request.POST['region1']
            city = request.POST['region2']
            place =  request.POST['dest']
            guides = Guide.objects.filter(Gui_place = place)#返回攻略的集合
            return render_to_response('guide.html',{'guides':guides,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
        else:#返回查询的表格
            return render_to_response('guide.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))         
def guideInfor(request):#展示攻略
    if ('memberid'  in request.session) or ('admin_id' in request.session):#logined 
        guide_id = request.GET['guide_id']
        guide = Guide.objects.get(id = guide_id)
        answers = guide.a_guide.all()
        return render_to_response('guide_details.html',{'guide':guide,'logined':'1','answers':answers,'User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def guideAdd(request):#增加攻略
    if 'memberid' in request.session:#logined 
        if 'province' in request.POST:
            province = request.POST['region1']
            city = request.POST['region2']
            place =  request.POST['dest']
            title = request.POST['title']
            context  = request.POST['context']
            Guide.objects.create(Gui_province = province, Gui_city = city, Gui_place  = place, Gui_title  = title,
                                 Gui_context = context, Gui_author_id = request.session['memberid'])
            return render_to_response('guide_search_form.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
        else:
            return render_to_response('guide_form.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1',},context_instance=RequestContext(request))     
def forum(request):#论坛
    if 'memberid' in request.session:#logined 
        return render_to_response('forum.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))    
def file(request):#上传文件
    if request.method == "POST":        
            name = request.POST['name']
            headImg = request.FILES['pic']
            File.objects.create(name = name, headImg = headImg)
            return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))
    else:
        pic = File.objects.get(id = 7)
        return render_to_response('file.html',{'pic':pic,'flag':0},context_instance=RequestContext(request))
def user_find(request,team_id):#查找队员
    if 'memberid' in request.session:#logined 
        if request.method == "POST":
            name = request.POST['name']
            users0 = User.objects.filter(Username = name)
            users1 = User.objects.filter(Email = name)
            users = []
            for user in users0:
                users.append(user)
            for user in users1:
                users.append(user)
            return render_to_response('userfind_form.html',{'logined':'1','users':users,'team_id':team_id,'User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request)) 
        else:
            return render_to_response('userfind_form.html',{'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance = RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def check(request,team_id,flag) :#审核团队
    if 'memberid' in request.session:#logined 
        id = request.session['memberid']
        user = User.objects.get(id = id)
        team = Team.objects.get(id = team_id)
        if flag == '0':
            team.User0.add(user)
            team.User1.remove(user)           
        elif flag == '1':
            team.User1.remove(user)
        teams = user.my_team.all()
        team0 = user.team0.all()
        team1 = user.team1.all()
        team2 = user.team2.all()
        return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
        'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def check1(request,userid,team_id,flag) :#审核队员
    if 'memberid' in request.session:#logined 
        user1 = User.objects.get(id = int(userid))
        team = Team.objects.get(id = team_id)
        if flag == '0':
            team.User0.add(user1)
            team.User2.remove(user1)           
        elif flag == '1':
            team.User2.remove(user1)
        user = User.objects.get(id = request.session['memberid'])
        teams = user.my_team.all()
        team0 = user.team0.all()
        team1 = user.team1.all()
        team2 = user.team2.all()
        return render_to_response('team.html',{'teams':teams,'team0':team0,'team1':team1,
        'team2':team2,'logined':'1','User':user},context_instance= RequestContext(request))
    else:#unlogin
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request)) 
def pinglun1(request):
    if 'memberid' in request.session:
        context = request.POST['text']
        author_id = request.session['memberid']
        diary_id =request.POST['diary_id']
        Ans_diary.objects.create(Ans_context = context,Ans_author_id = author_id,diary_id = diary_id)
        return render_to_response("home.html",context_instance=RequestContext(request))  
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def pinglun2(request):
    if 'memberid' in request.session:
        context = request.POST['text']
        author_id = request.session['memberid']
        guide_id =request.POST['guide_id']
        Ans_guide.objects.create(Ans_context = context,Ans_author_id = author_id,guide_id = guide_id) 
        return render_to_response("home.html",context_instance=RequestContext(request))   
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def ch_img(request):
    if 'memberid' in request.session:
        id = request.session['memberid']
        if request.method == "POST":
            if "pic" in request.FILES:
                pic = request.FILES['pic']
            else:
                pic = 'load/defailt.jpg'
            user = User.objects.get(id = id)
            user.Img = pic
            user.save()
            return render_to_response('home.html',{'logined':'1','User':user},context_instance=RequestContext(request))  
        else:
            return render_to_response('img_form.html',context_instance=RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def admin_login(request):#管理员登陆
    if request.method =="POST":
        email = request.POST['email']
        password = request.POST['password']
        adminer = Admin.objects.get(Email = email)
        if adminer:
            if adminer.password == password:
                request.session['admin_id'] = adminer.id
                return render_to_response('admin_home.html',context_instance=RequestContext(request))
            else:
                return render_to_response('admin_login.html',context_instance=RequestContext(request))
        else:
            return render_to_response('admin_login.html',context_instance=RequestContext(request))        
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
def admin_logout(request) :#管理员退出
    if 'admin_id' in request.session:
        del request.session['admin_id']
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
def admin_finduser(request):
    if 'admin_id' in request.session:
        if request.method == "POST":
            email = request.POST['email']
            user0 =User.objects.get(Email = email)
            return render_to_response('admin_finduser.html',{'user0':user0},context_instance=RequestContext(request)) 
        else:
            return render_to_response('admin_finduser.html',{'users':'0'},context_instance=RequestContext(request)) 
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request)) 
def admin_resetpass(request):
    if 'admin_id' in request.session:
       userid = request.GET['user_id']
       user = User.objects.get(id = userid)
       if user:
            user.Password = '123456'
            user.save()
       return render_to_response('admin_home.html',context_instance=RequestContext(request))
       
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
def admin_guide(request):
    if 'admin_id' in request.session:
        guides = Guide.objects.all()
        return render_to_response('admin_guide.html',{'guides':guides},context_instance=RequestContext(request))
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
def admin_guidecheck(request):
    if 'admin_id' in request.session:
        guide_id = request.GET['guide_id']
        guide = Guide.objects.get(id = guide_id)
        guide.Gui_flag = True
        guide.save()
        guides = Guide.objects.all()
        return render_to_response('admin_guide.html',{'guides':guides},context_instance=RequestContext(request))
    else:
        return render_to_response('admin_login.html',context_instance=RequestContext(request))
def admin(request):
    return render_to_response('admin_home.html',context_instance=RequestContext(request))
def guide_zan(request):
    id = request.GET['guide_id']
    guide = Guide.objects.get(id = id)
    answers = guide.a_guide.all()
    gid = str(guide.id)
    if gid in request.session:
        return render_to_response('guide_details.html',{'guide':guide,'logined':'1','answers':answers,'User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
    else:
        n = guide.Gui_zan
        guide.Gui_zan = n + 1
        guide.save()
        request.session[gid] = '1'
        return render_to_response('guide_details.html',{'guide':guide,'logined':'1','answers':answers,'User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
def myguide(request):
    if 'memberid' in request.session:
        id = request.session['memberid']
        guides = User.objects.get(id = id).guide.all()
        return render_to_response('myguides.html',{'guides':guides,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def guide_change(request):
    if 'memberid' in request.session:
        guide_id = request.GET['guide_id']
        guide = Guide.objects.get(id = guide_id)
        if 'title' in request.POST:
            province = request.POST['region1']
            city = request.POST['region2']
            title = request.POST['title']
            context = request.POST['text']
            guide.Gui_province = province
            guide.Gui_city = city
            guide.Gui_title = title
            guide.Gui_context = context
            guide.save()
            answers = guide.a_guide.all()
            return render_to_response('guide_details.html',{'guide':guide,'logined':'1','answers':answers,'User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
        else:            
            return render_to_response('guide_change.html',{'guide':guide,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
def guide_delete(request):
    if 'memberid' in request.session:
        guide_id = request.GET['guide_id']
        id = request.session['memberid']        
        guide = Guide.objects.get(id = guide_id)
        answers = guide.a_guide.all()
        for answer in answers:
            answer.delete()
        guide.delete()
        guides = User.objects.get(id = id).guide.all()
        return render_to_response('myguides.html',{'guides':guides,'logined':'1','User':User.objects.get(id = request.session['memberid'])},context_instance=RequestContext(request))
    else:
        return render_to_response('login.html',{'un_login':'1'},context_instance=RequestContext(request))  
    
   
    
    