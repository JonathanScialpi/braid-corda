package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.unwrap

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class EchoFlow(private val message: String, private val otherParty: Party) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): String {
        return if (otherParty != ourIdentity) {
            val session = initiateFlow(otherParty)
            session.sendAndReceive<String>(message).unwrap { it }
        } else {
            createResponse(message, ourIdentity)
        }
    }
}

@InitiatedBy(EchoFlow::class)
class ResponderEchoFlow(private val session: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val msg = session.receive<String>().unwrap { it }
        val response = createResponse(msg, ourIdentity)
        session.send(response)
    }

}


@Suppress("NOTHING_TO_INLINE")
private inline fun createResponse(msg: String, identity: Party) = "Hi, it's ${identity.name} echoing your message: $msg"

