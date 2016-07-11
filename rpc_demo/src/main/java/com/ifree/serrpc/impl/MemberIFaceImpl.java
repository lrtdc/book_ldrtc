package com.ifree.serrpc.impl;

import org.apache.avro.AvroRemoteException;

import com.ifree.serrpc.builder.MemberIFace;
import com.ifree.serrpc.builder.Members;
import com.ifree.serrpc.builder.Retmsg;

/**
* 具体的业务处理类
* @author Administrator
*
*/
public class MemberIFaceImpl implements MemberIFace {

     final String userName="rita";
     final String userPwd="888888";
     /**
     * 登录业务处理
     */
     @Override
     public Retmsg login(Members m) throws AvroRemoteException {
          //验证登录权限
          if(m.getUserName().equals(userName)&&m.getUserPwd().equals(userPwd)){
               return new Retmsg("恭喜你，登录成功，欢迎进入AVRO测试环境。");
          }
          return new Retmsg("对不起，权限不足，不能登录。");
     }

}
