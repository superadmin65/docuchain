package com.ipfs.app.solidity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class SimpleStorage extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6104d38061001e6000396000f30060606040526004361061004b5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663de7939fd8114610050578063e942b51614610118575b600080fd5b341561005b57600080fd5b6100a160046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506101ad95505050505050565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100dd5780820151838201526020016100c5565b50505050905090810190601f16801561010a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561012357600080fd5b6101ab60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405281815292919060208401838380828437509496506102bd95505050505050565b005b6101b56103d5565b6000826040518082805190602001908083835b602083106101e75780518252601f1990920191602091820191016101c8565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102b15780601f10610286576101008083540402835291602001916102b1565b820191906000526020600020905b81548152906001019060200180831161029457829003601f168201915b50505050509050919050565b6102c56103e7565b6040805190810160405280848152602001838152509050826000846040518082805190602001908083835b6020831061030f5780518252601f1990920191602091820191016102f0565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405190819003902090805161035492916020019061040c565b50816000846040518082805190602001908083835b602083106103885780518252601f199092019160209182019101610369565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001019080516103cf92916020019061040c565b50505050565b60206040519081016040526000815290565b60408051908101604052806103fa6103d5565b81526020016104076103d5565b905290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061044d57805160ff191683800117855561047a565b8280016001018555821561047a579182015b8281111561047a57825182559160200191906001019061045f565b5061048692915061048a565b5090565b6104a491905b808211156104865760008155600101610490565b905600a165627a7a72305820a27fbcd50f17b490ef9889ee9b4b5eb0badf50f396dd2ce087f99d0e1e6ff9c40029";

    protected SimpleStorage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimpleStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> getDocumentByName(String _documentName) {
        Function function = new Function("getDocumentByName", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentName)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> set(String _documentName, String _hash) {
        Function function = new Function(
                "set", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentName), 
                new org.web3j.abi.datatypes.Utf8String(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<SimpleStorage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleStorage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SimpleStorage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleStorage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static SimpleStorage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SimpleStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
