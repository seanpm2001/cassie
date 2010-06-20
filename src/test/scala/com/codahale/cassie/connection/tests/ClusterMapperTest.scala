package com.codahale.cassie.connection.tests

import org.scalatest.matchers.MustMatchers
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfterAll, Spec}
import com.codahale.cassie.tests.util.MockCassandraServer
import com.codahale.cassie.connection.ClusterMapper
import java.net.InetSocketAddress

class ClusterMapperTest extends Spec with MustMatchers with BeforeAndAfterAll {
  val server = new MockCassandraServer(MockCassandraServer.choosePort())
  when(server.cassandra.get_string_property("token map")).thenReturn("""{"blahblah":"c1.example.com","bleeblee":"c2.example.com"}""")

  override protected def beforeAll() {
    server.start()
  }

  override protected def afterAll() {
    server.stop()
  }

  describe("mapping a cluster") {
    it("returns the set of nodes in the cluster") {
      val mapper = new ClusterMapper("127.0.0.1", server.port)

      mapper.hosts() must equal(Set(
        addr("c1.example.com", server.port), addr("c2.example.com", server.port)
      ))
    }
  }

  def addr(host: String, port: Int) = new InetSocketAddress(host, port)
}
