/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package gnu.java.zrtp.jmf.transform.srtp;

import gnu.java.zrtp.jmf.transform.PacketTransformer;
import gnu.java.zrtp.jmf.transform.TransformEngine;

/**
 * SRTPTransformEngine class implements TransformEngine interface.
 * It stores important information / objects regarding SRTP processing.
 * Through SRTPTransformEngine, we can get the needed PacketTransformer, which
 * will be used by abstract TransformConnector classes. 
 * 
 * @author Bing SU (nova.su@gmail.com)
 *
 */
public class SRTPTransformEngine
    implements TransformEngine
{
    /**
     * The default SRTPCryptoContext, which will be used to derive other
     * contexts.
     */
    private SRTPCryptoContext defaultContext;

    /**
     * The default SRTPCryptoContext, which will be used to derive other
     * contexts.
     */
    private SRTCPCryptoContext defaultContextControl;
    /**
     * Construct a SRTPTransformEngine based on given master encryption key,
     * master salt key and SRTP/SRTCP policy.
     * 
     * @param masterKey
     *            the master encryption key
     * @param masterSalt
     *            the master salt key
     * @param srtpPolicy
     *            SRTP policy
     * @param srtcpPolicy
     *            SRTCP policy
     */
    public SRTPTransformEngine(byte[] masterKey, byte[] masterSalt,
            SRTPPolicy srtpPolicy, SRTPPolicy srtcpPolicy) 
    {
        /*
         * Don't store secret key material here, it's not required.
         * The SRT(C)P crypto contexts store them internally until not longer needed. 
         */
        defaultContext = new SRTPCryptoContext(0, 0, 0, masterKey, masterSalt, srtpPolicy);

        defaultContextControl = new SRTCPCryptoContext(0, masterKey, masterSalt, srtcpPolicy);
    }

    /**
     * Close the transformer engine.
     * 
     * The close functions closes all stored default crypto contexts. This deletes key data
     * and forces a cleanup of the crypto contexts.
     */
    public void close()
    {
        if (defaultContext != null)
            defaultContext.close();
        if (defaultContextControl != null)
            defaultContextControl.close();

        defaultContext = null;
        defaultContextControl = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.java.sip.communicator.impl.media.transform.
     *      TransformEngine#getRTCPTransformer()
     */
    public PacketTransformer getRTCPTransformer() {
        return new SRTCPTransformer(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.java.sip.communicator.impl.media.transform.
     *      TransformEngine#getRTPTransformer()
     */
    public PacketTransformer getRTPTransformer() {
        return new SRTPTransformer(this);
    }

    /**
     * Get the default SRTPCryptoContext
     *
     * @return the default SRTPCryptoContext
     */
    public SRTPCryptoContext getDefaultContext() {
        return this.defaultContext;
    }

    /**
     * Get the default SRTPCryptoContext
     *
     * @return the default SRTPCryptoContext
     */
    public SRTCPCryptoContext getDefaultContextControl() {
        return this.defaultContextControl;
    }

}
