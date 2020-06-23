package org.hyperledger.fabric.example;

import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;
import io.netty.handler.ssl.OpenSsl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;


import static java.nio.charset.StandardCharsets.UTF_8;

public class TwoChaincode extends ChaincodeBase {

    private static Log _logger = LogFactory.getLog(TwoChaincode.class);

    @Override
    public Response init(ChaincodeStub stub) {
        try {
            System.out.println("********初始化java版链码啦。。。。");
            String func = stub.getFunction();

            if (!func.equals("init")) {
                return newErrorResponse("不支持init以外的功能");
            }
            List<String> args = stub.getParameters();
            for(int i = 0;i < args.size(); i+=2){
                System.out.println("*** key :"+args.get(i) + "*** val :" + args.get(i+1));
                stub.putStringState(args.get(i), args.get(i+1));
            }


            return newSuccessResponse();
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            _logger.info("调用Java简单Chaincode");
            String func = stub.getFunction();
            List<String> params = stub.getParameters();
            if (func.equals("move")) {
                return move(stub, params);
            }
            if (func.equals("delete")) {
                return delete(stub, params);
            }
            if (func.equals("query")) {
                return query(stub, params);
            }
            if (func.equals("add")) {
                return add(stub, params);
            }
            return newErrorResponse("无效的调用函数名称。期望以下之一：[\"move\", \"delete\", \"query\",\"add\"]");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response move(ChaincodeStub stub, List<String> args) {
        if (args.size() != 3) {
            return newErrorResponse("参数数目不正确。期待3");
        }
        String accountFromKey = args.get(0);
        String accountToKey = args.get(1);

        String accountFromValueStr = stub.getStringState(accountFromKey);
        if (accountFromValueStr == null) {
            return newErrorResponse(String.format("实体 %s 未找到", accountFromKey));
        }
        int accountFromValue = Integer.parseInt(accountFromValueStr);

        String accountToValueStr = stub.getStringState(accountToKey);
        if (accountToValueStr == null) {
            return newErrorResponse(String.format("实体 %s 未找到", accountToKey));
        }
        int accountToValue = Integer.parseInt(accountToValueStr);

        int amount = Integer.parseInt(args.get(2));

        if (amount > accountFromValue) {
            return newErrorResponse(String.format("帐户中没有足够的钱 %s", accountFromKey));
        }

        accountFromValue -= amount;
        accountToValue += amount;

        _logger.info(String.format("A的新值： %s", accountFromValue));
        _logger.info(String.format("B的新值： %s", accountToValue));

        stub.putStringState(accountFromKey, Integer.toString(accountFromValue));
        stub.putStringState(accountToKey, Integer.toString(accountToValue));

        _logger.info("转移完成");

        Map<String, byte[]> transientMap = stub.getTransient();
        if (null != transientMap) {
            if (transientMap.containsKey("event") && transientMap.get("event") != null) {
                stub.setEvent("event", transientMap.get("event"));
            }
            if (transientMap.containsKey("result") && transientMap.get("result") != null) {
                return newSuccessResponse(transientMap.get("result"));
            }
        }
        return newSuccessResponse();
//        return   newSuccessResponse("invoke finished successfully", ByteString.copyFrom(accountFromKey + ": " + accountFromValue + " " + accountToKey + ": " + accountToValue, UTF_8).
//
//                        toByteArray());
    }

    //从状态中删除实体
    private Response delete(ChaincodeStub stub, List<String> args) {
        if (args.size() != 1) {
            return newErrorResponse("参数数目不正确。应为1");
        }
        String key = args.get(0);
        // 从分类帐中的状态中删除密钥
        stub.delState(key);
        return newSuccessResponse();
    }
    //添加
    private Response add(ChaincodeStub stub, List<String> args) {
        // 将指定的值和密钥写入帐本
        for(int i = 0;i < args.size(); i+=2){
            System.out.println("*** key :"+args.get(i) + "*** val :" + args.get(i+1));
            stub.putStringState(args.get(i), args.get(i+1));
        }
        return newSuccessResponse();
    }

    // 表示链码查询的查询回调
    private Response query(ChaincodeStub stub, List<String> args) {
        if (args.size() != 1) {
            return newErrorResponse("参数数目不正确。需要查询的人的姓名");
        }
        String key = args.get(0);
        //byte[] stateBytes
        String val = stub.getStringState(key);
        if (val == null) {
            return newErrorResponse(String.format("Error: state for %s is null", key));
        }
        _logger.info(String.format("查询响应:\nName: %s, 数量: %s\n", key, val));
        return newSuccessResponse(val, ByteString.copyFrom(val, UTF_8).toByteArray());
    }

    public static void main(String[] args) {
        System.out.println("O666penSSL666: " + OpenSsl.isAvailable());
        new TwoChaincode().start(args);
    }

}
